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

import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseDTO;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.service.CourseService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseService courseService;

	@GetMapping()
	public ResponseEntity<List<Course>> getAllCourses() {
		Optional<List<Course>> courses = Optional.ofNullable(courseService.getAllCourses());
		if (courses.isPresent()) {
			return new ResponseEntity<>(courses.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping()
	public ResponseEntity<Course> createCourse(@RequestBody Course course) {
		Optional<Course> createdCourse = Optional.ofNullable(courseService.addCourse(course));
		if (createdCourse.isPresent()) {
			return new ResponseEntity<>(createdCourse.get(), HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{courseId}")
	public ResponseEntity<Boolean> deleteCourse(@PathVariable Integer courseId) {
		Optional<Boolean> deletedCourse = Optional.of(courseService.deleteCourse(courseId));
		if (deletedCourse.isPresent()) {
			return new ResponseEntity<>(deletedCourse.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{courseId}")
	public ResponseEntity<Course> updateCourse(@RequestBody Course course, @PathVariable Integer courseId) {
		Optional<Course> updatedCourse = Optional.of(courseService.updateCourse(course, courseId));
		if (updatedCourse.isPresent()) {
			return new ResponseEntity<>(updatedCourse.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/by-batch-program-teacher")
	public ResponseEntity<Object> getCoursesByBatchProgramAndTeacher(
			@RequestBody BatchProgramCourseDTO batchProgramCourseDTO) {
		try {
			List<Course> courses = courseService.getCoursesByBatchProgramAndTeacher(batchProgramCourseDTO);
			return ResponseUtil.buildSuccessResponse(courses);
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error fetching evaluations: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
