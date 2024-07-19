package com.cozentus.trainingtrackingapplication.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.dto.EvaluationStudentDTO;
import com.cozentus.trainingtrackingapplication.model.EvaluationStudent;
import com.cozentus.trainingtrackingapplication.service.EvaluationStudentService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/evaluationStudent")
public class EvaluationStudentController {

	private final EvaluationStudentService evaluationStudentService;

	public EvaluationStudentController(EvaluationStudentService evaluationStudentService) {
		this.evaluationStudentService = evaluationStudentService;
	}

	@PostMapping
	public ResponseEntity<Object> addEvaluationStudent(@RequestBody EvaluationStudentDTO evaluationStudent) {
		try {
			EvaluationStudent createdEvaluationStudent = evaluationStudentService
					.addEvaluationStudent(evaluationStudent);
			if (createdEvaluationStudent != null) {
				return ResponseUtil.buildSuccessResponse(Collections.singletonList(createdEvaluationStudent));
			} else {
				return ResponseUtil.buildErrorResponse("Error creating evaluationStudent", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

	@PostMapping("/saveAll")
	public ResponseEntity<Object> addAllEvaluationStudent(@RequestBody List<EvaluationStudent> evaluationStudent) {
		try {
			List<EvaluationStudent> createdEvaluationStudent = evaluationStudentService
					.addMultipleEvaluationStudent(evaluationStudent);
			if (!createdEvaluationStudent.isEmpty()) {
				return ResponseUtil.buildSuccessResponse(Collections.singletonList(createdEvaluationStudent));
			} else {
				return ResponseUtil.buildErrorResponse("Error creating evaluationStudent", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}
}
