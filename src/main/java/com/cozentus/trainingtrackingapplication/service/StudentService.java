package com.cozentus.trainingtrackingapplication.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.repository.StudentRepository;

@Service
public class StudentService {

	private static final String DUPLICATE = "A Student with the same code already exists.";

	private StudentRepository studentRepository;

	StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Boolean doesStudentCodeExist(String studentCode) {
		if (studentRepository.existsByStudentCode(studentCode)) {
			throw new DataIntegrityViolationException(DUPLICATE);
		}
		return false;
	}

	public Student addStudent(Student student) {
		return studentRepository.save(student);
	}

	public Student editStudent(Integer studentId, Student student) {
		if (studentRepository.existsByStudentCode(student.getStudentCode())) {
			throw new DataIntegrityViolationException(DUPLICATE);
		}
		Optional<Student> updatedStudent = studentRepository.findById(studentId);
		if (updatedStudent.isPresent()) {
			Student studentToUpdate = updatedStudent.get();
			studentToUpdate.setStudentName(student.getStudentName());
			studentToUpdate.setStudentCode(student.getStudentCode());
			if (student.getStudentEmail() != null) {
				studentToUpdate.setStudentEmail(student.getStudentEmail());
			}
			return studentRepository.save(studentToUpdate);
		}
		return null;
	}

	public Boolean deleteStudent(Integer studentId) {
		Optional<Student> deletedStudent = studentRepository.findById(studentId);
		if (deletedStudent.isPresent()) {
			studentRepository.delete(deletedStudent.get());
			return true;
		}
		return false;
	}

	public Set<Student> getStudentsByBatchAndProgram(Integer batchId, Integer programId) {
		return studentRepository.findStudentsByBatchIdAndProgramId(batchId, programId);
	}
}
