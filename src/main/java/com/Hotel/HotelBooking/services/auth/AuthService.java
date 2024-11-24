package com.Hotel.HotelBooking.services.auth;

import com.Hotel.HotelBooking.dto.SignupRequest;
import com.Hotel.HotelBooking.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);
}
