package com.Hotel.HotelBooking.controller.auth;


import com.Hotel.HotelBooking.dto.SignupRequest;
import com.Hotel.HotelBooking.dto.UserDto;
import com.Hotel.HotelBooking.services.auth.AuthService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<?>signUser(@RequestBody SignupRequest signupRequest) {
        try{
            UserDto createdUser = authService.createUser(signupRequest);
            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        }
        catch(EntityExistsException entityExistsException){
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        catch(Exception e){
            return new ResponseEntity<>("User not created, come again later", HttpStatus.BAD_REQUEST);
        }
    }
}
