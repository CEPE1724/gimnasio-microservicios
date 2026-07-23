package com.util.class_booking_service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassBookingRequestDto(

        @NotBlank(message = "El username del entrenador es obligatorio")
        String trainerUsername,

        @NotBlank(message = "El nombre de la clase es obligatorio")
        String className,

        @NotNull(message = "La fecha de la clase es obligatoria")
        @Future(message = "La fecha de la clase debe ser futura")
        LocalDateTime classDate

) {
}
