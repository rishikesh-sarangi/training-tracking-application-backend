package com.cozentus.trainingtrackingapplication.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseTeacherDTO;
import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseTeacherDeleteDTO;
import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseTeacherResponse;
import com.cozentus.trainingtrackingapplication.service.BatchProgramCourseTeacherService;
import com.cozentus.trainingtrackingapplication.service.BatchService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/batch-course-teacher")
public class BatchProgramCourseTeacherController {

	private BatchProgramCourseTeacherService batchProgramCourseTeacherService;

	private BatchService batchService;

	BatchProgramCourseTeacherController(BatchProgramCourseTeacherService batchProgramCourseTeacherService,
			BatchService batchService) {
		this.batchProgramCourseTeacherService = batchProgramCourseTeacherService;
		this.batchService = batchService;
	}

	@PostMapping("/update")
	public ResponseEntity<Object> updateBatchCourseTeacher(@RequestBody BatchProgramCourseTeacherResponse dto) {
		try {
			Boolean response = batchProgramCourseTeacherService.updateBatchProgramCourseTeacher(dto);
			if (response.equals(true)) {
				return ResponseUtil.buildSuccessResponse(Collections.emptyList());
			} else {
				return ResponseUtil.buildErrorResponse("Batch not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}

	}

	@GetMapping("/{batchId}/{programId}")
	public ResponseEntity<List<BatchProgramCourseTeacherDTO>> getCourseAndTeacherByBatchAndProgram(
			@PathVariable Integer batchId, @PathVariable Integer programId) {
		try {
			List<BatchProgramCourseTeacherDTO> result = batchProgramCourseTeacherService
					.getCourseAndTeacherByBatchAndProgram(batchId, programId);
			if (!result.isEmpty()) {
				return ResponseEntity.ok(result);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

//    this is a delete function even tho we are using post mapping
	@PostMapping
	public ResponseEntity<Object> deleteBatchProgramCourseTeacherByBatchId(
			@RequestBody BatchProgramCourseTeacherDeleteDTO dto) {
		try {
			Boolean response = batchService.deleteByBatchIdAndProgramIdAndCourseIdAndTeacherId(dto);
			if (response.equals(true)) {
				return ResponseUtil.buildSuccessResponse(Collections.emptyList());
			} else {
				return ResponseUtil.buildErrorResponse("Batch not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}
}
