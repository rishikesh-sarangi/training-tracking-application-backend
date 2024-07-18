package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private BatchService batchService;

	@GetMapping
	public ResponseEntity<List<Batch>> getAllCourses() {
		Optional<List<Batch>> batches = Optional.ofNullable(batchService.getAllBatches());
		if (batches.isPresent()) {
			return new ResponseEntity<>(batches.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<Batch> createCourse(@RequestBody Batch batch) {
		Optional<Batch> createdBatch = Optional.ofNullable(batchService.addBatch(batch));
		if (createdBatch.isPresent()) {
			return new ResponseEntity<>(createdBatch.get(), HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{batchId}")
	public ResponseEntity<Batch> updateBatch(@PathVariable Integer batchId, @RequestBody Batch batch) {
		Optional<Batch> updatedBatch = Optional.ofNullable(batchService.editBatch(batchId, batch));
		if (updatedBatch.isPresent()) {
			return new ResponseEntity<>(updatedBatch.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

//	delete the batch itself
	@Transactional
	@DeleteMapping("/{batchId}")
	public ResponseEntity<Boolean> deleteBatch(@PathVariable Integer batchId) {
		batchService.deleteBatchProgramCourseTeacherByBatchId(batchId);
		batchService.deleteBatch(batchId);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

//	delete the program associated with that particular batch
	@Transactional
	@DeleteMapping("/{batchId}/programs/{programId}")
	public ResponseEntity<Boolean> deleteProgram(@PathVariable Integer batchId, @PathVariable Integer programId) {
		batchService.deleteProgramAndStudentsForBatch(programId, batchId);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

//	get the programs and associated students under a certain batch
	@GetMapping("/{batchId}/programs-and-students")
	public ResponseEntity<List<ProgramDTO>> getProgramsWithStudentsByBatchId(@PathVariable Integer batchId) {
		List<ProgramDTO> programsInfo = batchService.getProgramsWithStudentsByBatchId(batchId);
		return ResponseEntity.ok(programsInfo);
	}

//	get the courses under the program in a batch
	@GetMapping("/{batchId}/program/{programId}/courses-teachers")
	public ResponseEntity<List<CourseWithTeachersDTO>> getCoursesAndTeachersByBatchAndProgram(
			@PathVariable Integer batchId, @PathVariable Integer programId) {
		List<CourseWithTeachersDTO> courses = batchService.getCoursesAndTeachersByBatchAndProgram(batchId, programId);
		return ResponseEntity.ok(courses);
	}

}
