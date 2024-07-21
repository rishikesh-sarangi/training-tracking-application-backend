package com.cozentus.trainingtrackingapplication.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/enrollments")
public class EnrollmentController {

	@Autowired
	private EnrollmentService enrollmentService;

	@PostMapping
	public ResponseEntity<Object> enrollStudents(@RequestBody EnrollmentDTO enrollmentDTO) {
		try {
			Boolean response = enrollmentService.enrollStudents(enrollmentDTO);
			if (response.equals(true)) {
				return ResponseUtil.buildSuccessResponse(Collections.emptyList());
			} else {
				return ResponseUtil.buildErrorResponse("Student cannot be enrolled", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

	@PutMapping
	public ResponseEntity<Object> updateEnrollment(@RequestBody EnrollmentDTO enrollmentUpdateDTO) {
		try {
			Boolean response = enrollmentService.updateEnrollment(enrollmentUpdateDTO);
			if (response.equals(true)) {
				return ResponseUtil.buildSuccessResponse(Collections.emptyList());
			} else {
				return ResponseUtil.buildErrorResponse("Student cannot be enrolled", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}
}
