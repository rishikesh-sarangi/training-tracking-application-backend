package com.cozentus.trainingtrackingapplication.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.EnrollmentDTO;

import com.cozentus.trainingtrackingapplication.model.Batch;
import com.cozentus.trainingtrackingapplication.model.Program;
import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.ProgramRepository;
import com.cozentus.trainingtrackingapplication.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class EnrollmentService {

	private StudentRepository studentRepository;

	private BatchRepository batchRepository;

	private ProgramRepository programRepository;

	private BatchService batchService;

	EnrollmentService(StudentRepository studentRepository, BatchRepository batchRepository,
			ProgramRepository programRepository, BatchService batchService) {

		this.studentRepository = studentRepository;
		this.batchRepository = batchRepository;
		this.programRepository = programRepository;
		this.batchService = batchService;
	}

	public void enrollStudents(EnrollmentDTO enrollmentDTO) {
		Batch batch = batchRepository.findById(enrollmentDTO.getBatchId()).orElseThrow(
				() -> new IllegalArgumentException("Batch not found with id: " + enrollmentDTO.getBatchId()));
		Program program = programRepository.findById(enrollmentDTO.getProgramId()).orElseThrow(
				() -> new IllegalArgumentException("Program not found with id: " + enrollmentDTO.getProgramId()));

		List<Student> students = studentRepository
				.findAllById(enrollmentDTO.getStudents().stream().map(Student::getStudentId).toList());

		// Update batch-program relationship
		batch.getPrograms().add(program);
		program.getBatches().add(batch);

		// Update student-batch and student-program relationships
		for (Student student : students) {
			student.setBatch(batch);
			student.getPrograms().add(program);
			program.getStudents().add(student);
		}

		// Save all entities
		batchRepository.save(batch);
		programRepository.save(program);
		studentRepository.saveAll(students);
	}

	@Transactional
	public void updateEnrollment(EnrollmentDTO enrollmentDTO) {
		Batch batch = batchRepository.findById(enrollmentDTO.getBatchId()).orElseThrow(
				() -> new IllegalArgumentException("Batch not found with id: " + enrollmentDTO.getBatchId()));

		Program newProgram = programRepository.findById(enrollmentDTO.getProgramId()).orElseThrow(
				() -> new IllegalArgumentException("Program not found with id: " + enrollmentDTO.getProgramId()));

		List<Student> newStudents = studentRepository
				.findAllById(enrollmentDTO.getStudents().stream().map(Student::getStudentId).toList());

		// Check if the program is changing
		boolean isProgramChanging = !batch.getPrograms().contains(newProgram);

		if (isProgramChanging) {
			// Program is changing, so delete the old program association and add the new
			// one
			deleteProgramAndStudentsForBatch(enrollmentDTO.getOldProgramId(), batch.getBatchId());
			addNewProgramToBatch(batch, newProgram, newStudents);
		} else {
			// Program is not changing, only update student associations
			updateStudentAssociations(batch, newProgram, newStudents);
		}

		// Save entities
		batchRepository.save(batch);
		programRepository.save(newProgram);
		studentRepository.saveAll(newStudents);
	}

	private void deleteProgramAndStudentsForBatch(Integer oldProgramId, Integer batchId) {
		batchService.deleteProgramAndStudentsForBatch(oldProgramId, batchId);
		batchRepository.deleteByBatchAndProgramId(batchId, oldProgramId);
		// Add any other necessary deletion logic
	}

	private void addNewProgramToBatch(Batch batch, Program newProgram, List<Student> newStudents) {
		batch.getPrograms().add(newProgram);
		newProgram.getBatches().add(batch);

		for (Student student : newStudents) {
			student.setBatch(batch);
			student.getPrograms().add(newProgram);
			newProgram.getStudents().add(student);
		}
	}

	private void updateStudentAssociations(Batch batch, Program program, List<Student> newStudents) {
		// Remove students no longer associated with this program in this batch
		Set<Student> studentsToRemove = program.getStudents().stream()
				.filter(s -> s.getBatch().equals(batch) && !newStudents.contains(s)).collect(Collectors.toSet());

		for (Student student : studentsToRemove) {
			student.getPrograms().remove(program);
			if (student.getPrograms().isEmpty()) {
				student.setBatch(null);
			}
		}
		program.getStudents().removeAll(studentsToRemove);

		// Add new students to the program
		for (Student student : newStudents) {
			if (!program.getStudents().contains(student)) {
				student.setBatch(batch);
				student.getPrograms().add(program);
				program.getStudents().add(student);
			}
		}

		studentRepository.saveAll(studentsToRemove);
	}
}
