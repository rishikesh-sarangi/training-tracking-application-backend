package com.cozentus.trainingtrackingapplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Program;
import com.cozentus.trainingtrackingapplication.model.Teacher;
import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.ProgramRepository;
import com.cozentus.trainingtrackingapplication.repository.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private ProgramService programService;

	public Course addCourse(Course course) {
		return courseRepository.save(course);
	}

	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	public Course updateCourse(Course courseDetails, Integer id) {
		Optional<Course> optionalCourse = courseRepository.findById(id);
		if (optionalCourse.isPresent()) {
			Course existingCourse = optionalCourse.get();
			existingCourse.setCode(courseDetails.getCode());
			existingCourse.setCourseName(courseDetails.getCourseName());
			existingCourse.setDescription(courseDetails.getDescription());
			existingCourse.setPracticeTime(courseDetails.getPracticeTime());
			existingCourse.setTheoryTime(courseDetails.getTheoryTime());
			return courseRepository.save(existingCourse);
		} else {
			return null;
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
}