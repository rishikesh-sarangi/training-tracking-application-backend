package com.cozentus.trainingtrackingapplication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.Batch;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Program;
import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.ProgramRepository;
import com.cozentus.trainingtrackingapplication.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProgramService {

	private ProgramRepository programRepository;

	private CourseRepository courseRepository;

	private BatchRepository batchRepository;

	private StudentRepository studentRepository;

	ProgramService(ProgramRepository programRepository, CourseRepository courseRepository,
			BatchRepository batchRepository, StudentRepository studentRepository) {

		this.programRepository = programRepository;
		this.courseRepository = courseRepository;
		this.batchRepository = batchRepository;
		this.studentRepository = studentRepository;
	}

	public List<Program> getAllPrograms() {
		return programRepository.findAll();
	}

	/*
	 * since courses is now a detached entity for the program we need to convert it
	 * back to a managed entity
	 */

	public Program addProgram(Program program) {
		List<Course> managedCourses = new ArrayList<>();
		for (Course course : program.getCourses()) {
			Course managedCourse = courseRepository.findById(course.getCourseId()).orElseThrow(
					() -> new EntityNotFoundException("Course not found with id: " + course.getCourseId()));
			managedCourses.add(managedCourse);
		}
		program.setCourses(managedCourses);
		return programRepository.save(program);
	}

	public Program editProgram(Integer programId, Program program) {
		Optional<Program> updatedProgramOpt = programRepository.findById(programId);

		if (updatedProgramOpt.isPresent()) {
			Program programToUpdate = updatedProgramOpt.get();
			List<Course> existingCourses = programToUpdate.getCourses();
			List<Course> newCourses = program.getCourses();

			// Find and delete excluded old courses
			for (Course existingCourse : existingCourses) {
				boolean isExcluded = newCourses.stream()
						.noneMatch(newCourse -> newCourse.getCourseId().equals(existingCourse.getCourseId()));
				if (isExcluded) {
					batchRepository.deleteByProgramIdAndCourseId(programId, existingCourse.getCourseId());
				}
			}

			// Update the program details
			programToUpdate.setProgramName(program.getProgramName());
			programToUpdate.setProgramCode(program.getProgramCode());
			programToUpdate.setDescription(program.getDescription());
			programToUpdate.setTheoryTime(program.getTheoryTime());
			programToUpdate.setPracticeTime(program.getPracticeTime());

			// Update courses only if they are different
			boolean areCoursesDifferent = !existingCourses.equals(newCourses);
			if (areCoursesDifferent) {
				programToUpdate.setCourses(newCourses);
			}

			return programRepository.save(programToUpdate);
		}

		return null;
	}

	@Transactional
	public Boolean deleteProgram(Integer programId) {
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new EntityNotFoundException("Program not found"));

		// Remove program from all associated batches
		for (Batch batch : program.getBatches()) {
			batch.getPrograms().remove(program);
			batchRepository.save(batch);
		}

		// Remove program from all associated students
		for (Student student : program.getStudents()) {
			student.getPrograms().remove(program);
			if (student.getPrograms().isEmpty()) {
				student.setBatch(null);
			}
			studentRepository.save(student);
		}

		// Remove program from all associated courses
		for (Course course : program.getCourses()) {
			course.getProgram().remove(program);
			courseRepository.save(course);
		}

		// delete entries in batch_program_course_teacher table
		batchRepository.deleteByProgramId(programId);

		// delete the program
		programRepository.delete(program);

		return true;
	}
}
