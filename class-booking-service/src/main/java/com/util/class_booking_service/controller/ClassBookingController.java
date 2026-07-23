package com.util.class_booking_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.util.class_booking_service.dto.ClassBookingRequestDto;
import com.util.class_booking_service.dto.ClassBookingResponseDto;
import com.util.class_booking_service.service.ClassBookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class ClassBookingController {

    private final ClassBookingService classBookingService;

    public ClassBookingController(
            ClassBookingService classBookingService
    ) {
        this.classBookingService = classBookingService;
    }

    @PostMapping
    public ResponseEntity<ClassBookingResponseDto> createBooking(
            @Valid @RequestBody ClassBookingRequestDto request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String memberUsername =
                jwt.getClaimAsString("preferred_username");

        ClassBookingResponseDto created =
                classBookingService.createBooking(
                        request,
                        memberUsername
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<ClassBookingResponseDto>> getMyBookings(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String memberUsername =
                jwt.getClaimAsString("preferred_username");

        return ResponseEntity.ok(
                classBookingService.getBookingsByMember(
                        memberUsername
                )
        );
    }

    @GetMapping("/my-classes")
    public ResponseEntity<List<ClassBookingResponseDto>> getMyClasses(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String trainerUsername =
                jwt.getClaimAsString("preferred_username");

        return ResponseEntity.ok(
                classBookingService.getBookingsByTrainer(
                        trainerUsername
                )
        );
    }

    @PatchMapping("/{id}/attend")
    public ResponseEntity<ClassBookingResponseDto> attendBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String trainerUsername =
                jwt.getClaimAsString("preferred_username");

        ClassBookingResponseDto updated =
                classBookingService.attendBooking(
                        id,
                        trainerUsername
                );

        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<ClassBookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(
                classBookingService.getAllBookings()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id
    ) {
        classBookingService.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }
}
