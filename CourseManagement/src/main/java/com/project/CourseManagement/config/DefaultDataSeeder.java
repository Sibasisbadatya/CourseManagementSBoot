package com.project.CourseManagement.config;

import com.project.CourseManagement.dto.UserDTO;
import com.project.CourseManagement.entity.Role;
import com.project.CourseManagement.enums.UserRole;
import com.project.CourseManagement.repository.RolesRepository;
import com.project.CourseManagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
public class DefaultDataSeeder {

    @Bean
    public ApplicationRunner initializeRoles(RolesRepository rolesRepository){
        return args->{
            for(UserRole userRole:UserRole.values()){
                Role newRole = new Role();
                newRole.setName(userRole);
                rolesRepository.save(newRole);
            }
        };
    }

//    @Bean
//    public ApplicationRunner seedAdmin(UserService userService){
//        return (args)->{
//            List<UserDTO> admins = List.of(
//                    new UserDTO("Aarav Sharma", "aarav@gmail.com","siba@123", 9114738401L, Set.of(UserRole.USER)),                    new UserDTO("Vivaan Patel", "vivaan@gmail.com", "siba@123", 9114738402L, Set.of(UserRole.ADMIN)),
//                    new UserDTO("Aditya Verma", "aditya@gmail.com", "siba@123", 9114738403L, Set.of(UserRole.USER, UserRole.ADMIN)),
//                    new UserDTO("Krishna Reddy", "krishna@gmail.com", "siba@123", 9114738404L, Set.of(UserRole.USER)),
//                    new UserDTO("Arjun Singh", "arjun@gmail.com", "siba@123", 9114738405L, Set.of(UserRole.USER)),
//                    new UserDTO("Rohan Gupta", "rohan@gmail.com", "siba@123", 9114738406L, Set.of(UserRole.ADMIN)),
//                    new UserDTO("Ishaan Kumar", "ishaan@gmail.com", "siba@123", 9114738407L, Set.of(UserRole.USER, UserRole.ADMIN)),
//                    new UserDTO("Sai Teja", "sai@gmail.com", "siba@123", 9114738408L, Set.of(UserRole.USER)),
//                    new UserDTO("Rahul Das", "rahul@gmail.com", "siba@123", 9114738409L, Set.of(UserRole.USER)),
//                    new UserDTO("Ankit Mishra", "ankit@gmail.com", "siba@123", 9114738410L, Set.of(UserRole.ADMIN)),
//
//                    new UserDTO("Priya Sharma", "priya@gmail.com", "siba@123", 9114738411L, Set.of(UserRole.USER)),
//                    new UserDTO("Ananya Patel", "ananya@gmail.com", "siba@123", 9114738412L, Set.of(UserRole.USER)),
//                    new UserDTO("Sneha Reddy", "sneha@gmail.com", "siba@123", 9114738413L, Set.of(UserRole.USER, UserRole.ADMIN)),
//                    new UserDTO("Pooja Singh", "pooja@gmail.com", "siba@123", 9114738414L, Set.of(UserRole.USER)),
//                    new UserDTO("Neha Gupta", "neha@gmail.com", "siba@123", 9114738415L, Set.of(UserRole.ADMIN)),
//                    new UserDTO("Aditi Verma", "aditi@gmail.com","siba@123", 9114738416L, Set.of(UserRole.USER)),
//                    new UserDTO("Kavya Nair", "kavya@gmail.com", "siba@123",9114738417L, Set.of(UserRole.USER)),
//                    new UserDTO("Meera Joshi", "meera@gmail.com","siba@123", 9114738418L, Set.of(UserRole.USER, UserRole.ADMIN)),
//                    new UserDTO("Riya Das", "riya@gmail.com","siba@123", 9114738419L, Set.of(UserRole.USER)),
//                    new UserDTO("Diya Kapoor", "diya@gmail.com","siba@123", 9114738420L, Set.of(UserRole.ADMIN)),
//                    new UserDTO("Karan Malhotra", "karan@gmail.com","siba@123", 9114738421L, Set.of(UserRole.USER)),
//                    new UserDTO("Mohit Jain", "mohit@gmail.com","siba@123", 9114738422L, Set.of(UserRole.USER)),
//                    new UserDTO("Nikhil Rao", "nikhil@gmail.com", "siba@123",9114738423L, Set.of(UserRole.USER, UserRole.ADMIN)),
//                    new UserDTO("Harsh Vardhan", "harsh@gmail.com","siba@123", 9114738424L, Set.of(UserRole.USER)),
//                    new UserDTO("Manish Yadav", "manish@gmail.com", "siba@123",9114738425L, Set.of(UserRole.ADMIN)),
//                    new UserDTO("Deepak Sharma", "deepak@gmail.com", "siba@123",9114738426L, Set.of(UserRole.USER)),
//                    new UserDTO("Suresh Kumar", "suresh@gmail.com","siba@123", 9114738427L, Set.of(UserRole.USER)),
//                    new UserDTO("Vikram Reddy", "vikram@gmail.com","siba@123", 9114738428L, Set.of(UserRole.USER, UserRole.ADMIN)),
//                    new UserDTO("Ajay Singh", "ajay@gmail.com", "siba@123",9114738429L, Set.of(UserRole.USER)),
//                    new UserDTO("Rakesh Gupta", "rakesh@gmail.com","siba@123", 9114738430L, Set.of(UserRole.ADMIN))
//            );
//            admins.forEach(admin->{
//                try{
//                    userService.registerUser(admin);
//                } catch (RuntimeException e) {
//                    log.info("SIBASIS BADATYA");
//                    log.info(e.getMessage());
//                    throw new RuntimeException(e);
//                }
//            });
//        };
//    }


}
