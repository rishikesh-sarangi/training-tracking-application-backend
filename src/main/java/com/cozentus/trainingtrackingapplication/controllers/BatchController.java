package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.dto.CourseWithTeachersDTO;
import com.cozentus.trainingtrackingapplication.dto.ProgramDTO;
import com.cozentus.trainingtrackingapplication.model.Batch;
import com.cozentus.trainingtrackingapplication.service.BatchService;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/batches")
public class BatchController {

	private final BatchService batchService;

	BatchController(BatchService batchService) {
		this.batchService = batchService;
	}

	@GetMapping
	public ResponseEntity<List<Batch>> getAllBatches() {
		Optional<List<Batch>> batches = Optional.ofNullable(batchService.getAllBatches());
		return batches.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) {
		Optional<Batch> createdBatch = Optional.ofNullable(batchService.addBatch(batch));
		return createdBatch.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PutMapping("/{batchId}")
	public ResponseEntity<Batch> updateBatch(@PathVariable Integer batchId, @RequestBody Batch batch) {
		Optional<Batch> updatedBatch = Optional.ofNullable(batchService.editBatch(batchId, batch));
		return updatedBatch.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@Transactional
	@DeleteMapping("/{batchId}")
	public ResponseEntity<Boolean> deleteBatch(@PathVariable Integer batchId) {
		try {
			batchService.deleteBatchProgramCourseTeacherByBatchId(batchId);
			batchService.deleteBatch(batchId);
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}

	}

	@Transactional
	@DeleteMapping("/{batchId}/programs/{programId}")
	public ResponseEntity<Boolean> deleteProgram(@PathVariable Integer batchId, @PathVariable Integer programId) {
		try {
			batchService.deleteProgramAndStudentsForBatch(programId, batchId);
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{batchId}/programs-and-students")
	public ResponseEntity<List<ProgramDTO>> getProgramsWithStudentsByBatchId(@PathVariable Integer batchId) {
		Optional<List<ProgramDTO>> programsInfo = Optional
				.ofNullable(batchService.getProgramsWithStudentsByBatchId(batchId));
		return programsInfo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/{batchId}/program/{programId}/courses-teachers")
	public ResponseEntity<List<CourseWithTeachersDTO>> getCoursesAndTeachersByBatchAndProgram(
			@PathVariable Integer batchId, @PathVariable Integer programId) {
		Optional<List<CourseWithTeachersDTO>> courses = Optional
				.ofNullable(batchService.getCoursesAndTeachersByBatchAndProgram(batchId, programId));
		return courses.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
