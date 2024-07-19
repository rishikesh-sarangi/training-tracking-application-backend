package com.cozentus.trainingtrackingapplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseTeacherDTO;
import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseTeacherResponse;
import com.cozentus.trainingtrackingapplication.repository.BatchProgramCourseTeacherRepository;
import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.ProgramRepository;
import com.cozentus.trainingtrackingapplication.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class BatchProgramCourseTeacherService {

	private BatchRepository batchRepository;

	private CourseRepository courseRepository;

	private TeacherRepository teacherRepository;

	private ProgramRepository programRepository;

	private BatchProgramCourseTeacherRepository batchProgramCourseTeacherRepository;

	BatchProgramCourseTeacherService(BatchProgramCourseTeacherRepository batchProgramCourseTeacherRepository,
			BatchRepository batchRepository, CourseRepository courseRepository, TeacherRepository teacherRepository,
			ProgramRepository programRepository) {
		this.batchProgramCourseTeacherRepository = batchProgramCourseTeacherRepository;
		this.batchRepository = batchRepository;
		this.courseRepository = courseRepository;
		this.teacherRepository = teacherRepository;
		this.programRepository = programRepository;
	}

	@Transactional
	public void updateBatchProgramCourseTeacher(BatchProgramCourseTeacherResponse dto) {

		// verify that the entities exist
		batchRepository.findById(dto.getBatchId()).orElseThrow(() -> new EntityNotFoundException("Batch not found"));

		programRepository.findById(dto.getProgramId())
				.orElseThrow(() -> new EntityNotFoundException("Program not found"));

		courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new EntityNotFoundException("Course not found"));

		teacherRepository.findById(dto.getTeacherId())
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

		// innsert into junction table using custom query
		batchProgramCourseTeacherRepository.insertBatchProgramCourseTeacher(dto.getBatchId(), dto.getProgramId(),
				dto.getCourseId(), dto.getTeacherId());
	}

	public List<BatchProgramCourseTeacherDTO> getCourseAndTeacherByBatchAndProgram(Integer batchId, Integer programId) {
		List<Object[]> results = batchRepository.findCourseAndTeacherByBatchAndProgram(batchId, programId);
		return results.stream().map(obj -> new BatchProgramCourseTeacherDTO((Integer) obj[0], (String) obj[1],
				(String) obj[2], (Integer) obj[3], (String) obj[4])).toList();
	}
}
