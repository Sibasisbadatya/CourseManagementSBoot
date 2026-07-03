package com.project.CourseManagement.config;

import com.project.CourseManagement.filters.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users/register","/users/login").permitAll()
//                                .requestMatchers("/audio-ws/**").permitAll()
                                .requestMatchers("/error").permitAll()
//                                .requestMatchers("/audio/**").permitAll()
//                                .anyRequest().permitAll()
//                                .requestMatchers("/assignments/stream/**", "/error").permitAll()
                                .anyRequest().authenticated()
                );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
//        Your function is creating a **custom configuration for Spring MVC to allow CORS (Cross-Origin Resource Sharing)** so that your **React frontend (running on `localhost:5173`) can communicate with your Spring Boot backend**. Normally, browsers block requests between different origins (different domain/port/protocol) for security reasons. Since your frontend and backend run on different ports, the browser would block API calls unless the backend explicitly allows them. By defining a `@Bean` that returns a `WebMvcConfigurer`, you are telling Spring Boot to extend its default MVC configuration. Inside it, you override the `addCorsMappings` method, which lets you define CORS rules using the `CorsRegistry`.
//        When the application starts, Spring detects this bean and applies the CORS configuration globally. The line `registry.addMapping("/**")` means that the CORS rules apply to **all API endpoints** in your backend. The `allowedOrigins("http://localhost:5173")` specifies that **only requests coming from your React development server** are allowed to access the backend APIs. The `allowedMethods("GET","POST","PUT","DELETE","OPTIONS")` defines which HTTP methods the frontend can use when calling your APIs; `OPTIONS` is included because browsers send a **preflight OPTIONS request** before certain cross-origin requests to check permissions. The `allowedHeaders("*")` means the server accepts **any request headers**, such as `Content-Type` or `Authorization` (commonly used for JWT tokens). Finally, `allowCredentials(true)` allows the browser to **send credentials like cookies or authentication tokens** with cross-origin requests, which is necessary if you are using things like **HttpOnly refresh token cookies or session cookies**.
//        In practice, when your React app makes a request to the Spring Boot API, the browser first checks these CORS rules. If the request originates from `localhost:5173`, uses an allowed method, and sends permitted headers, the server responds with CORS headers that tell the browser the request is safe. The browser then proceeds with the actual API call and can also include cookies or authentication information if needed. Without this configuration, the browser would block the request with a **CORS policy error**, even though the backend itself is reachable.

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all paths
                        .allowedOriginPatterns("http://localhost:*") // Your frontend URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

//    OPTIONS is an HTTP method used by the browser to check what operations are allowed on a server resource before sending the real request.
//    It is mainly used for CORS preflight requests.

//    Why Browsers Send OPTIONS
//    When a frontend (like React) makes a cross-origin request, the browser first asks the server:
//            “Is it okay if I send this request?”
//    It does this using an OPTIONS request.

//            | Configuration      | Purpose                     |
//            | ------------------ | --------------------------- |
//            | CORS               | allow cross-origin requests |
//            | Interceptors       | run logic before controller |
//            | Static resources   | configure file paths        |
//            | Message converters | JSON/XML handling           |



//            | Method                         | Purpose                       |
//            | ------------------------------ | ----------------------------- |
//            | `addCorsMappings()`            | Configure CORS                |
//            | `addInterceptors()`            | Add request interceptors      |
//            | `addViewControllers()`         | Map URLs to views             |
//            | `configureMessageConverters()` | Customize JSON/XML converters |
//            | `addResourceHandlers()`        | Configure static resources    |


}


