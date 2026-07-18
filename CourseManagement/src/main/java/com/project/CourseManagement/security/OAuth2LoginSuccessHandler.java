package com.project.CourseManagement.security;

import com.project.CourseManagement.entity.Role;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.enums.UserRole;
import com.project.CourseManagement.repository.RolesRepository;
import com.project.CourseManagement.repository.UserRepository;
import com.project.CourseManagement.utils.JWTUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OAuth2 Login Success Handler
 * 
 * This handler is called when a user successfully authenticates via OAuth2 (GitHub)
 * It performs the following:
 * 1. Extracts user information from OAuth2 provider (GitHub)
 * 2. Creates or updates user in database
 * 3. Generates JWT token
 * 4. Redirects to frontend with token
 */
@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final JWTUtils jwtUtils;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, 
                                     RolesRepository rolesRepository, 
                                     JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        try {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oauthToken.getPrincipal();
            
            // Log all attributes to see what GitHub is sending
            log.info("OAuth2 attributes: {}", oAuth2User.getAttributes());
            
            // Extract user information from OAuth2 provider
            String email = extractEmail(oAuth2User);
            String name = extractName(oAuth2User);
            String avatarUrl = extractAvatarUrl(oAuth2User);
            
            log.info("OAuth2 login successful for email: {}, name: {}", email, name);
            
            // Find or create user in database
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                log.info("Creating new user from OAuth2: {}", email);
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setProfileImage(avatarUrl);
                // OAuth users don't have password (handled by OAuth provider)
                newUser.setPassword(null);
                
                // Assign default USER role (student)
                Role studentRole = rolesRepository.findByName(UserRole.USER)
                        .orElseThrow(() -> new RuntimeException("Role USER not found in database. Please run: INSERT INTO roles (name) VALUES ('USER');"));
                
                List<Role> roles = new ArrayList<>();
                roles.add(studentRole);
                newUser.setRoles(roles);
                
                return userRepository.save(newUser);
            });
            
            // Update existing user's profile image if changed
            if (user.getProfileImage() == null || !user.getProfileImage().equals(avatarUrl)) {
                user.setProfileImage(avatarUrl);
                userRepository.save(user);
            }
            
            // Generate JWT token
            String jwtToken = jwtUtils.generateToken(user.getEmail());
            
            log.info("Generated JWT token for user: {}", email);
            
            // Redirect to frontend with token and user info
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/auth/callback")
                    .queryParam("token", jwtToken)
                    .queryParam("email", user.getEmail())
                    .queryParam("name", user.getName())
                    .queryParam("profileImage", user.getProfileImage())
                    .build()
                    .toUriString();
            
            log.info("Redirecting to: {}", targetUrl);
            
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            
        } catch (Exception e) {
            log.error("Error during OAuth2 authentication success handling", e);
            log.error("Exception type: {}", e.getClass().getName());
            log.error("Exception message: {}", e.getMessage());
            
            // Redirect to frontend with error
            String errorUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/auth")
                    .queryParam("error", "oauth_failed")
                    .queryParam("message", e.getMessage())
                    .build()
                    .toUriString();
            
            getRedirectStrategy().sendRedirect(request, response, errorUrl);
        }
    }
    
    /**
     * Extract email from OAuth2 user attributes
     * GitHub provides email in the attributes map
     */
    private String extractEmail(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        // Try to get email directly
        String email = (String) attributes.get("email");
        
        // If email is null, try to get from login (GitHub username)
        if (email == null || email.isEmpty()) {
            String login = (String) attributes.get("login");
            email = login + "@github.com"; // Fallback email
        }
        
        return email;
    }
    
    /**
     * Extract name from OAuth2 user attributes
     */
    private String extractName(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String name = (String) attributes.get("name");
        
        // If name is null, use login (username)
        if (name == null || name.isEmpty()) {
            name = (String) attributes.get("login");
        }
        
        return name;
    }
    
    /**
     * Extract avatar URL from OAuth2 user attributes
     */
    private String extractAvatarUrl(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return (String) attributes.get("avatar_url");
    }
}
