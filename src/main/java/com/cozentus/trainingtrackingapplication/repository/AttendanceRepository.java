package com.cozentus.trainingtrackingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

}
