package com.Hotel.HotelBooking.services.auth;

import com.Hotel.HotelBooking.dto.SignupRequest;
import com.Hotel.HotelBooking.dto.UserDto;
import com.Hotel.HotelBooking.entity.User;
import com.Hotel.HotelBooking.enums.UserRole;
import com.Hotel.HotelBooking.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

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
            System.out.println("Admin account created successfully");
        }
        else{
            System.out.println("Admin account already exists");
        }
    }
    public UserDto createUser(SignupRequest signupRequest){
        if(userRepository.findFirstByEmail(signupRequest.getEmail()).isPresent()){
            throw new EntityExistsException("User Already Present With Email "+signupRequest.getEmail());
        }
        User user=new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setUserRole(UserRole.CUSTOMER);
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        User createUser = userRepository.save(user);
        return createUser.getUserDto();
    }
}
