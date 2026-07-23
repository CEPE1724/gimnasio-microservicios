package com.util.class_booking_service.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.util.class_booking_service.dto.ClassBookingRequestDto;
import com.util.class_booking_service.dto.ClassBookingResponseDto;
import com.util.class_booking_service.model.BookingStatus;
import com.util.class_booking_service.model.ClassBooking;
import com.util.class_booking_service.repository.ClassBookingRepository;
import com.util.class_booking_service.service.ClassBookingService;

@Service
public class ClassBookingServiceImpl implements ClassBookingService {

    private final ClassBookingRepository classBookingRepository;

    public ClassBookingServiceImpl(
            ClassBookingRepository classBookingRepository
    ) {
        this.classBookingRepository = classBookingRepository;
    }

    @Override
    public ClassBookingResponseDto createBooking(
            ClassBookingRequestDto request,
            String memberUsername
    ) {
        ClassBooking booking = new ClassBooking();

        booking.setMemberUsername(memberUsername);
        booking.setTrainerUsername(request.trainerUsername());
        booking.setClassName(request.className());
        booking.setClassDate(request.classDate());
        booking.setStatus(BookingStatus.RESERVED);

        ClassBooking savedBooking =
                classBookingRepository.save(booking);

        return toResponseDto(savedBooking);
    }

    @Override
    public List<ClassBookingResponseDto> getBookingsByMember(
            String memberUsername
    ) {
        return classBookingRepository
                .findByMemberUsername(memberUsername)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ClassBookingResponseDto> getBookingsByTrainer(
            String trainerUsername
    ) {
        return classBookingRepository
                .findByTrainerUsername(trainerUsername)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ClassBookingResponseDto attendBooking(
            Long bookingId,
            String trainerUsername
    ) {
        ClassBooking booking = classBookingRepository
                .findById(bookingId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No se encontro la reserva con id: "
                                        + bookingId
                        )
                );

        if (!booking.getTrainerUsername().equals(trainerUsername)) {
            throw new IllegalStateException(
                    "El entrenador autenticado no puede marcar esta reserva"
            );
        }

        if (booking.getStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException(
                    "Solo se pueden marcar como atendidas las reservas en estado RESERVED. "
                            + "Estado actual: " + booking.getStatus()
            );
        }

        booking.setStatus(BookingStatus.ATTENDED);

        ClassBooking updatedBooking =
                classBookingRepository.save(booking);

        return toResponseDto(updatedBooking);
    }

    @Override
    public List<ClassBookingResponseDto> getAllBookings() {
        return classBookingRepository
                .findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public void deleteBooking(
            Long bookingId
    ) {
        if (!classBookingRepository.existsById(bookingId)) {
            throw new NoSuchElementException(
                    "No se encontro la reserva con id: "
                            + bookingId
            );
        }

        classBookingRepository.deleteById(bookingId);
    }

    private ClassBookingResponseDto toResponseDto(
            ClassBooking booking
    ) {
        return new ClassBookingResponseDto(
                booking.getId(),
                booking.getMemberUsername(),
                booking.getTrainerUsername(),
                booking.getClassName(),
                booking.getClassDate(),
                booking.getStatus()
        );
    }
}
