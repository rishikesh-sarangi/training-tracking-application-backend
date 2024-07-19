package com.cozentus.trainingtrackingapplication.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.dto.EnrollmentDTO;
import com.cozentus.trainingtrackingapplication.service.EnrollmentService;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/enrollments")
public class EnrollmentController {

	private EnrollmentService enrollmentService;

	EnrollmentController(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	@PostMapping
	public ResponseEntity<Boolean> enrollStudents(@RequestBody EnrollmentDTO enrollmentDTO) {
		enrollmentService.enrollStudents(enrollmentDTO);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Boolean> updateEnrollment(@RequestBody EnrollmentDTO enrollmentUpdateDTO) {
		enrollmentService.updateEnrollment(enrollmentUpdateDTO);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
