package com.cozentus.trainingtrackingapplication.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.model.Attendance;
import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.service.AttendanceService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/attendance")
public class AttendanceController {
	
	@Autowired
	private AttendanceService attendanceService;
	
	@PostMapping
	public ResponseEntity<Object> createEvaluation(@RequestBody Attendance attendance) {
		try {
			Attendance createdAttendance = attendanceService.addAttendance(attendance);
			return ResponseUtil.buildSuccessResponse(Collections.singletonList(createdAttendance));
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error creating attendance entry: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> getAllAttendances() {
		try {
			return ResponseUtil.buildSuccessResponse(attendanceService.getAllAttendances());
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error fetching attendances: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
