package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
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
	public ResponseEntity<Boolean> updateBatchCourseTeacher(@RequestBody BatchProgramCourseTeacherResponse dto) {
		batchProgramCourseTeacherService.updateBatchProgramCourseTeacher(dto);
		return ResponseEntity.ok(true);
	}

	@GetMapping("/{batchId}/{programId}")
	public ResponseEntity<List<BatchProgramCourseTeacherDTO>> getCourseAndTeacherByBatchAndProgram(
			@PathVariable Integer batchId, @PathVariable Integer programId) {
		List<BatchProgramCourseTeacherDTO> result = batchProgramCourseTeacherService
				.getCourseAndTeacherByBatchAndProgram(batchId, programId);
		return ResponseEntity.ok(result);
	}

//    this is a delete function even tho we are using post mapping
	@PostMapping
	public ResponseEntity<Boolean> deleteBatchProgramCourseTeacherByBatchId(
			@RequestBody BatchProgramCourseTeacherDeleteDTO dto) {
//		Optional<Boolean> deletedBatchProgramCourseTeacher = Optional
		batchService.deleteByBatchIdAndProgramIdAndCourseIdAndTeacherId(dto);
		return ResponseEntity.ok(true);
	}
}
