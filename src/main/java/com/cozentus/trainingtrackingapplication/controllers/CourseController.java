package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
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
import com.cozentus.trainingtrackingapplication.dto.CoursesWithTopicsDTO;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.service.CourseService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

import jakarta.persistence.EntityNotFoundException;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/courses")
public class CourseController {

	private CourseService courseService;

	CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping()
	public ResponseEntity<List<Course>> getAllCourses() {
		Optional<List<Course>> courses = Optional.ofNullable(courseService.getAllCourses());
		if (courses.isPresent()) {
			return new ResponseEntity<>(courses.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<Object> createCourse(@RequestBody Course course) {
		try {
			Course savedCourse = courseService.addCourse(course);
			return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	public ResponseEntity<Object> updateCourse(@RequestBody Course course, @PathVariable Integer courseId) {
		try {
			Course updatedCourse = courseService.updateCourse(course, courseId);
			return new ResponseEntity<>(updatedCourse, HttpStatus.CREATED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/by-batch-program-teacher")
	public ResponseEntity<Object> getCoursesByBatchProgramAndTeacher(
			@RequestBody BatchProgramCourseDTO batchProgramCourseDTO) {
		try {
			List<CoursesWithTopicsDTO> courses = courseService
					.getCoursesWithTopicsByBatchProgramAndTeacher(batchProgramCourseDTO);
			return ResponseUtil.buildSuccessResponse(courses);
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error fetching evaluations: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
