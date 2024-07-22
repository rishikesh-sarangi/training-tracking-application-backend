package com.cozentus.trainingtrackingapplication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cozentus.trainingtrackingapplication.controllers.CourseController;
import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseDTO;
import com.cozentus.trainingtrackingapplication.dto.CoursesWithTopicsDTO;
import com.cozentus.trainingtrackingapplication.model.Course;

import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.ProgramRepository;
import com.cozentus.trainingtrackingapplication.repository.TeacherRepository;
import com.cozentus.trainingtrackingapplication.service.CourseService;
import com.cozentus.trainingtrackingapplication.service.ProgramService;

public class CourseTests {

	@InjectMocks
	private CourseService courseService;

	@Mock
	private CourseRepository courseRepository;

	@Mock
	private ProgramRepository programRepository;

	@Mock
	private TeacherRepository teacherRepository;

	@Mock
	private BatchRepository batchRepository;

	@Mock
	private ProgramService programService;

	@InjectMocks
	private CourseController courseController;

	@Mock
	private CourseService mockCourseService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// CourseService tests
	@Test
	void testAddCourse() {
		Course course = new Course();
		when(courseRepository.save(any(Course.class))).thenReturn(course);

		Course result = courseService.addCourse(course);

		assertNotNull(result);
		verify(courseRepository, times(1)).save(course);
	}

	@Test
	void testGetAllCourses1() {
		Course course1 = new Course();
		Course course2 = new Course();
		List<Course> courses = Arrays.asList(course1, course2);

		when(courseRepository.findAll()).thenReturn(courses);

		List<Course> result = courseService.getAllCourses();

		assertEquals(2, result.size());
		verify(courseRepository, times(1)).findAll();
	}

	@Test
	void testUpdateCourse1() {
		Course course = new Course();
		course.setCourseName("New Name");

		Course existingCourse = new Course();
		when(courseRepository.findById(anyInt())).thenReturn(Optional.of(existingCourse));
		when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);

		Course result = courseService.updateCourse(course, 1);

		assertNotNull(result);
		assertEquals("New Name", existingCourse.getCourseName());
		verify(courseRepository, times(1)).save(existingCourse);
	}

	// CourseController tests
	@Test
	void testCreateCourse() {
		Course course = new Course();
		when(mockCourseService.addCourse(any(Course.class))).thenReturn(course);

		ResponseEntity<Course> response = courseController.createCourse(course);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		verify(mockCourseService, times(1)).addCourse(course);
	}

	@Test
	void testGetAllCourses() {
		Course course = new Course();
		List<Course> courses = Collections.singletonList(course);
		when(mockCourseService.getAllCourses()).thenReturn(courses);

		ResponseEntity<List<Course>> response = courseController.getAllCourses();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(mockCourseService, times(1)).getAllCourses();
	}

	@Test
	void testDeleteCourse() {
		when(mockCourseService.deleteCourse(anyInt())).thenReturn(true);

		ResponseEntity<Boolean> response = courseController.deleteCourse(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(mockCourseService, times(1)).deleteCourse(1);
	}

	@Test
	void testUpdateCourse() {
		Course course = new Course();
		when(mockCourseService.updateCourse(any(Course.class), anyInt())).thenReturn(course);

		ResponseEntity<Course> response = courseController.updateCourse(course, 1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(mockCourseService, times(1)).updateCourse(course, 1);
	}

	@Test
	void testGetCoursesByBatchProgramAndTeacher() {
		BatchProgramCourseDTO dto = new BatchProgramCourseDTO();
		List<CoursesWithTopicsDTO> courses = new ArrayList<>();
		when(mockCourseService.getCoursesWithTopicsByBatchProgramAndTeacher(any(BatchProgramCourseDTO.class)))
				.thenReturn(courses);

		ResponseEntity<Object> response = courseController.getCoursesByBatchProgramAndTeacher(dto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(mockCourseService, times(1))
				.getCoursesWithTopicsByBatchProgramAndTeacher(any(BatchProgramCourseDTO.class));
	}
}