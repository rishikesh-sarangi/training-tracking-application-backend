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

import com.cozentus.trainingtrackingapplication.dto.TeacherBatchProgramCourseDTO;
import com.cozentus.trainingtrackingapplication.dto.TeacherEditDTO;
import com.cozentus.trainingtrackingapplication.model.Teacher;
import com.cozentus.trainingtrackingapplication.service.EmailService;
import com.cozentus.trainingtrackingapplication.service.TeacherService;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/teachers")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private EmailService emailService;

	@GetMapping
	public ResponseEntity<List<Teacher>> getAllCourses() {
		Optional<List<Teacher>> teachers = Optional.ofNullable(teacherService.getAllTeachers());
		if (teachers.isPresent()) {
			return new ResponseEntity<>(teachers.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

//	need to check if the email is in DB first or not
	@PostMapping
	public ResponseEntity<Boolean> createTeacher(@RequestBody Teacher teacher) {
		if (emailService.sendWelcomeEmailTeacher(teacher)) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{teacherId}")
	public ResponseEntity<Boolean> updateTeacher(@PathVariable Integer teacherId, @RequestBody TeacherEditDTO teacher) {
		if (emailService.sendWelcomeEmailTeacherEdit(teacherId, teacher)) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{teacherId}")
	public ResponseEntity<Boolean> deleteTeacher(@PathVariable Integer teacherId) {
		Optional<Boolean> deletedTeacher = Optional.of(teacherService.deleteTeacher(teacherId));
		if (deletedTeacher.isPresent()) {
			return new ResponseEntity<>(deletedTeacher.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{teacherEmail}")
	public ResponseEntity<Integer> getTeacherIdByTeacherEmail(@PathVariable String teacherEmail) {
		Optional<Integer> teacherId = Optional.ofNullable(teacherService.getTeacherId(teacherEmail));
		if (teacherId.isPresent()) {
			return new ResponseEntity<>(teacherId.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
    @GetMapping("/{teacherId}/batch-program-course-info")
    public ResponseEntity<List<TeacherBatchProgramCourseDTO>> getBatchProgramCourseInfo(@PathVariable Integer teacherId) {    	
    	Optional<List<TeacherBatchProgramCourseDTO>> info = Optional.ofNullable(teacherService.getBatchProgramCourseInfoByTeacherId(teacherId));
		if (info.isPresent()) {
			return new ResponseEntity<>(info.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}