package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.service.EmailService;
import com.cozentus.trainingtrackingapplication.service.StudentService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/students")
public class StudentController {

	private StudentService studentService;

	private EmailService emailService;

	StudentController(StudentService studentService, EmailService emailService) {

		this.studentService = studentService;
		this.emailService = emailService;
	}

	@GetMapping
	public ResponseEntity<List<Student>> getAllStudents() {
		Optional<List<Student>> students = Optional.ofNullable(studentService.getAllStudents());
		if (students.isPresent()) {
			return new ResponseEntity<>(students.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

//	need to check if the email is in DB first or not
	@PostMapping
	public ResponseEntity<Object> createStudent(@RequestBody Student student) {
		try {
			if (studentService.doesStudentCodeExist(student.getStudentCode()).equals(false)) {
				if (emailService.sendWelcomeEmailStudent(student)) {
					return saveStudent(student);
				} else {
					return ResponseUtil.buildErrorResponse("Error sending email !", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>("Something went wrong !", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/{studentId}")
	public ResponseEntity<Object> updateStudent(@PathVariable Integer studentId, @RequestBody Student student) {
		try {
			if (studentService.doesStudentCodeExist(student.getStudentCode()).equals(false)) {
				if (student.getStudentEmail() != null) {
					if (emailService.sendWelcomeEmailStudent(student)) {
						return editStudent(studentId, student);
					} else {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
					}
				} else {
					return editStudent(studentId, student);
				}
			}
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@DeleteMapping("/{studentId}")
	public ResponseEntity<Boolean> deleteStudent(@PathVariable Integer studentId) {
		Optional<Boolean> deletedStudent = Optional.ofNullable(studentService.deleteStudent(studentId));
		if (deletedStudent.isPresent() && deletedStudent.get().equals(true)) {
			return new ResponseEntity<>(deletedStudent.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

//	get the associated students under a certain program which is under a certain batch
	@GetMapping("/{batchId}/{programId}")
	public ResponseEntity<Set<Student>> getStudentsByBatchAndProgram(@PathVariable Integer batchId,
			@PathVariable Integer programId) {
		Set<Student> students = studentService.getStudentsByBatchAndProgram(batchId, programId);
		return ResponseEntity.ok(students);
	}

	private ResponseEntity<Object> editStudent(Integer studentId, Student student) {
		Student updatedStudent = studentService.editStudent(studentId, student);
		if (updatedStudent != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(updatedStudent);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	private ResponseEntity<Object> saveStudent(Student student) {
		Student createdStudent = studentService.addStudent(student);
		if (createdStudent != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}
}
