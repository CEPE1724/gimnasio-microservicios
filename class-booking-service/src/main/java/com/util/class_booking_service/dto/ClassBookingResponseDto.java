package com.util.class_booking_service.dto;

import java.time.LocalDateTime;

import com.util.class_booking_service.model.BookingStatus;

public record ClassBookingResponseDto(

        Long id,

        String memberUsername,

        String trainerUsername,

        String className,

        LocalDateTime classDate,

        BookingStatus status

) {
}
