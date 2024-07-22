package com.cozentus.trainingtrackingapplication.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseDTO;
import com.cozentus.trainingtrackingapplication.dto.CoursesWithTopicsDTO;
import com.cozentus.trainingtrackingapplication.dto.TopicDTO;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Program;
import com.cozentus.trainingtrackingapplication.model.Teacher;
import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.ProgramRepository;
import com.cozentus.trainingtrackingapplication.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CourseService {
	
	private static final String DUPLICATE = "Course with the same code or name already exists.";

	private CourseRepository courseRepository;

	private ProgramRepository programRepository;

	private TeacherRepository teacherRepository;

	private BatchRepository batchRepository;

	private ProgramService programService;

	CourseService(CourseRepository courseRepository, ProgramRepository programRepository,
			TeacherRepository teacherRepository, BatchRepository batchRepository, ProgramService programService) {
		this.courseRepository = courseRepository;
		this.programRepository = programRepository;
		this.teacherRepository = teacherRepository;
		this.batchRepository = batchRepository;
		this.programService = programService;
	}

	public Course addCourse(Course course) {
		try {
			return courseRepository.save(course);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(DUPLICATE);
		}
	}

	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	public Course updateCourse(Course courseDetails, Integer id) {
		Optional<Course> optionalCourse = courseRepository.findById(id);
		if (optionalCourse.isPresent()) {
			try {
				Course existingCourse = optionalCourse.get();
				existingCourse.setCode(courseDetails.getCode());
				existingCourse.setCourseName(courseDetails.getCourseName());
				existingCourse.setDescription(courseDetails.getDescription());
				existingCourse.setPracticeTime(courseDetails.getPracticeTime());
				existingCourse.setTheoryTime(courseDetails.getTheoryTime());
				return courseRepository.save(existingCourse);
			} catch (DataIntegrityViolationException e) {
				throw new DataIntegrityViolationException(DUPLICATE);
			}

		} else {
			throw new EntityNotFoundException("Course Not Found");
		}
	}

	@Transactional
	public Boolean deleteCourse(Integer courseId) {
		Optional<Course> course = courseRepository.findById(courseId);
		if (course.isPresent()) {
//			manually delete all programs from a particular course
			for (Program program : course.get().getProgram()) {
				program.getCourses().removeIf(c -> c.getCourseId().equals(courseId));

				if (program.getCourses().isEmpty()) {
					programService.deleteProgram(program.getProgramId());
					programRepository.deleteById(program.getProgramId());
				} else {
					programRepository.save(program);
				}
			}

//			manually delete all teachers from a particular course
			for (Teacher teacher : course.get().getTeachers()) {
				teacher.getCourses().removeIf(c -> c.getCourseId().equals(courseId));
				teacherRepository.save(teacher);
			}

			batchRepository.deleteByCourseId(courseId);

			courseRepository.deleteById(courseId);

			return true;
		}
		return null;
	}

	public List<CoursesWithTopicsDTO> getCoursesWithTopicsByBatchProgramAndTeacher(
			BatchProgramCourseDTO batchProgramCourseDTO) {
		Integer batchId = batchProgramCourseDTO.getBatchId();
		Integer programId = batchProgramCourseDTO.getProgramId();
		Integer teacherId = batchProgramCourseDTO.getTeacherId();
		List<Object[]> results = courseRepository.findCoursesWithTopicsByBatchProgramAndTeacher(batchId, programId,
				teacherId);
		Map<Integer, CoursesWithTopicsDTO> courseMap = new HashMap<>();

		for (Object[] row : results) {
			Integer courseId = (Integer) row[0];
			CoursesWithTopicsDTO course = courseMap.computeIfAbsent(courseId,
					id -> new CoursesWithTopicsDTO(id, (String) row[1], (String) row[2], (String) row[3],
							(Integer) row[4], (Integer) row[5], new ArrayList<>()));

			if (row[6] != null) { // If there's a topic
				course.getTopics().add(new TopicDTO((Integer) row[6], (String) row[7], (Integer) row[8],
						(Integer) row[9], (Integer) row[10]));
			}
		}
		return new ArrayList<>(courseMap.values());
	}
}
