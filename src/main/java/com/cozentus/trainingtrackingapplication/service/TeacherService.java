package com.cozentus.trainingtrackingapplication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.TeacherBatchProgramCourseDTO;
import com.cozentus.trainingtrackingapplication.dto.TeacherEditDTO;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Teacher;
import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TeacherService {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private MyUserService myUserService;

	@Autowired
	private BatchRepository batchRepository;

	public List<Teacher> getAllTeachers() {
		return teacherRepository.findAll();
	}

//	detached entity so i will to set each course manually for the teacher
	public Teacher addTeacher(Teacher teacher) {
		List<Course> managedCourses = new ArrayList<>();
		for (Course course : teacher.getCourses()) {
			Course managedCourse = courseRepository.findById(course.getCourseId()).orElseThrow(
					() -> new EntityNotFoundException("Course not found with id: " + course.getCourseId()));
			managedCourses.add(managedCourse);
		}
		teacher.setCourses(managedCourses);
		return teacherRepository.save(teacher);
	}

	public Teacher editTeacher(Integer teacherId, TeacherEditDTO teacher) {
		Optional<Teacher> updatedTeacher = teacherRepository.findById(teacherId);
		if (updatedTeacher.isPresent()) {
			Teacher teacherToUpdate = updatedTeacher.get();
			teacherToUpdate.setTeacherName(teacher.getTeacherName());
			teacherToUpdate.setCourses(teacher.getCourses());
			if (teacher.getNewEmail() != null) {
				teacherToUpdate.setTeacherEmail(teacher.getNewEmail());
			}
			return teacherRepository.save(teacherToUpdate);
		}

		return null;
	}

	public Boolean deleteTeacher(Integer teacherId) {
		Optional<Teacher> deletedTeacher = teacherRepository.findById(teacherId);
		if (deletedTeacher.isPresent()) {
			batchRepository.deleteByTeacherId(teacherId);
			myUserService.deleteByEmailId(deletedTeacher.get().getTeacherEmail());
			teacherRepository.delete(deletedTeacher.get());
			return true;
		}
		return false;
	}

	public Integer getTeacherId(String teacherEmail) {
		return teacherRepository.findTeacherIdByTeacherEmail(teacherEmail);
	}
	
	public List<TeacherBatchProgramCourseDTO> getBatchProgramCourseInfoByTeacherId(Integer teacherId) {
        return teacherRepository.findBatchProgramCourseInfoByTeacherId(teacherId);
    }
}