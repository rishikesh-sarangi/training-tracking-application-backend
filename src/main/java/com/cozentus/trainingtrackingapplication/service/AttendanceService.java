package com.cozentus.trainingtrackingapplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.Attendance;
import com.cozentus.trainingtrackingapplication.repository.AttendanceRepository;

@Service
public class AttendanceService {
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	public List<Attendance> getAllAttendances() {
		return attendanceRepository.findAll();
	}
	
	public Attendance addAttendance(Attendance attendance) {
		return attendanceRepository.save(attendance);
	}
}
