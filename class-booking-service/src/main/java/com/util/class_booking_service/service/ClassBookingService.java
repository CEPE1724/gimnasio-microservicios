package com.util.class_booking_service.service;

import java.util.List;

import com.util.class_booking_service.dto.ClassBookingRequestDto;
import com.util.class_booking_service.dto.ClassBookingResponseDto;

public interface ClassBookingService {

    ClassBookingResponseDto createBooking(
            ClassBookingRequestDto request,
            String memberUsername
    );

    List<ClassBookingResponseDto> getBookingsByMember(
            String memberUsername
    );

    List<ClassBookingResponseDto> getBookingsByTrainer(
            String trainerUsername
    );

    ClassBookingResponseDto attendBooking(
            Long bookingId,
            String trainerUsername
    );

    List<ClassBookingResponseDto> getAllBookings();

    void deleteBooking(
            Long bookingId
    );
}
