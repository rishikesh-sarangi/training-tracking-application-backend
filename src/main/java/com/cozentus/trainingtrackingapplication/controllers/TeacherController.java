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
	public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
		if (emailService.sendWelcomeEmailTeacher(teacher)) {
			return saveTeacher(teacher);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{teacherId}")
	public ResponseEntity<Teacher> updateTeacher(@PathVariable Integer teacherId, @RequestBody Teacher teacher) {
		if (teacher.getTeacherEmail() != null) {
			if (emailService.sendWelcomeEmailTeacher(teacher)) {
				return editTeacher(teacherId, teacher);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return editTeacher(teacherId, teacher);
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

	private ResponseEntity<Teacher> editTeacher(Integer teacherId, Teacher teacher) {
		Optional<Teacher> updatedTeacher = Optional.ofNullable(teacherService.editTeacher(teacherId, teacher));

		return updatedTeacher.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	private ResponseEntity<Teacher> saveTeacher(Teacher teacher) {
		Optional<Teacher> createdTeacher = Optional.ofNullable(teacherService.addTeacher(teacher));

		return createdTeacher.map(t -> ResponseEntity.status(HttpStatus.CREATED).body(t))
				.orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}
}
