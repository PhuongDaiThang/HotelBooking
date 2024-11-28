package com.Hotel.HotelBooking.dto;

import com.Hotel.HotelBooking.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;

    private long userId;

    private UserRole userRole;
}
