package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.service.EmailService;
import com.cozentus.trainingtrackingapplication.service.StudentService;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private EmailService emailService;

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
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		if (student.getStudentEmail() == null) {
			return ResponseEntity.badRequest().build();
		}
		if (emailService.sendWelcomeEmailStudent(student)) {
			return saveStudent(student);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{studentId}")
	public ResponseEntity<Student> updateStudent(@PathVariable Integer studentId, @RequestBody Student student) {
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

	@DeleteMapping("/{studentId}")
	public ResponseEntity<Boolean> deleteStudent(@PathVariable Integer studentId) {
		Optional<Boolean> deletedStudent = Optional.ofNullable(studentService.deleteStudent(studentId));
		if (deletedStudent.isPresent()) {
			return new ResponseEntity<>(deletedStudent.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
    @GetMapping("/{batchId}/{programId}")
    public ResponseEntity<Set<Student>> getStudentsByBatchAndProgram(
            @PathVariable Integer batchId,
            @PathVariable Integer programId) {
        Set<Student> students = studentService.getStudentsByBatchAndProgram(batchId, programId);
        return ResponseEntity.ok(students);
    }

	private ResponseEntity<Student> editStudent(Integer studentId, Student student) {
		Optional<Student> updatedStudent = Optional.ofNullable(studentService.editStudent(studentId, student));
		return updatedStudent.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	private ResponseEntity<Student> saveStudent(Student student) {
	    Student createdStudent = studentService.addStudent(student);
	    return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
	}
}
