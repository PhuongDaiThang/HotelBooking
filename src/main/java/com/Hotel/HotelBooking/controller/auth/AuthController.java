package com.Hotel.HotelBooking.controller.auth;


import com.Hotel.HotelBooking.dto.AuthenticationRequest;
import com.Hotel.HotelBooking.dto.AuthenticationResponse;
import com.Hotel.HotelBooking.dto.SignupRequest;
import com.Hotel.HotelBooking.dto.UserDto;
import com.Hotel.HotelBooking.entity.User;
import com.Hotel.HotelBooking.repository.UserRepository;
import com.Hotel.HotelBooking.services.auth.AuthService;
import com.Hotel.HotelBooking.services.jwt.UserService;
import com.Hotel.HotelBooking.util.JwtUtil;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:8080") // Địa chỉ của frontend Vue app

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final UserService userService;
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
    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        }
        catch(BadCredentialsException e){
            throw new BadCredentialsException("Invalid username or password", e);
        }
        final UserDetails userDetails=userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser=userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt=jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse=new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
            authenticationResponse.setUserId(optionalUser.get().getId());
        }
        return authenticationResponse;
    }

}
