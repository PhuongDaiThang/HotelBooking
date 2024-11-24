package com.Hotel.HotelBooking.services.auth;

import com.Hotel.HotelBooking.entity.User;
import com.Hotel.HotelBooking.enums.UserRole;
import com.Hotel.HotelBooking.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount(){
        Optional<User> adminAccount=userRepository.findFirstByEmail(UserRole.ADMIN.toString());
        if(adminAccount.isEmpty()){
            User user=new User();
            user.setEmail("admin@test.com");
            user.setName("Admin");
            user.setUserRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
        else{
            System.out.println("Admin account already exists");
        }
    }
}
