package com.util.class_booking_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.util.class_booking_service.model.ClassBooking;

@Repository
public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {

    List<ClassBooking> findByMemberUsername(String memberUsername);

    List<ClassBooking> findByTrainerUsername(String trainerUsername);
}
