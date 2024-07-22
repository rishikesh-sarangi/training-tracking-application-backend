package com.cozentus.trainingtrackingapplication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.TeacherBatchProgramCourseDTO;
import com.cozentus.trainingtrackingapplication.dto.TeacherEditDTO;
import com.cozentus.trainingtrackingapplication.model.Attendance;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Teacher;
import com.cozentus.trainingtrackingapplication.repository.AttendanceRepository;
import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TeacherService {

	private TeacherRepository teacherRepository;

	private CourseRepository courseRepository;

	private MyUserService myUserService;

	private BatchRepository batchRepository;

	private AttendanceRepository attendanceRepository;

	TeacherService(TeacherRepository teacherRepository, CourseRepository courseRepository, MyUserService myUserService,
			BatchRepository batchRepository, AttendanceRepository attendanceRepository) {

		this.attendanceRepository = attendanceRepository;
		this.teacherRepository = teacherRepository;
		this.courseRepository = courseRepository;
		this.myUserService = myUserService;
		this.batchRepository = batchRepository;
	}

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

	@Transactional
	public Teacher editTeacher(Integer teacherId, TeacherEditDTO teacher) {
		Teacher teacherToUpdate = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + teacherId));

		List<Course> existingCourses = new ArrayList<>(teacherToUpdate.getCourses());
		List<Course> newCourses = teacher.getCourses().stream()
				.map(course -> courseRepository.findById(course.getCourseId()).orElseThrow(
						() -> new EntityNotFoundException("Course not found with id: " + course.getCourseId())))
				.toList();

		boolean coursesChanged = false;

		// Check for excluded courses
		List<Course> coursesToRemove = new ArrayList<>();
		for (Course existingCourse : existingCourses) {
			if (!newCourses.contains(existingCourse)) {
				batchRepository.deleteByTeacherIdAndCourseId(teacherId, existingCourse.getCourseId());
				// Handle related Attendance records
				List<Attendance> attendancesToRemove = attendanceRepository.findByTeacherAndCourse(teacherToUpdate,
						existingCourse);
				attendanceRepository.deleteAll(attendancesToRemove);
				coursesToRemove.add(existingCourse);
				coursesChanged = true;
			}
		}

		// Remove excluded courses if any
		if (!coursesToRemove.isEmpty()) {
			teacherToUpdate.getCourses().removeAll(coursesToRemove);
		}

		// Check for new courses
		for (Course newCourse : newCourses) {
			if (!existingCourses.contains(newCourse)) {
				teacherToUpdate.getCourses().add(newCourse);
				coursesChanged = true;
			}
		}

		// Only update courses if there are changes
		if (coursesChanged) {
			// The courses have already been updated in-place, so we don't need to set them
			// again
			// This line ensures that the changes are recognized by the persistence context
			teacherRepository.save(teacherToUpdate);
		}

		// Update other teacher details
		boolean otherDetailsChanged = false;
		if (!teacherToUpdate.getTeacherName().equals(teacher.getTeacherName())) {
			teacherToUpdate.setTeacherName(teacher.getTeacherName());
			otherDetailsChanged = true;
		}
		if (teacher.getNewEmail() != null && !teacherToUpdate.getTeacherEmail().equals(teacher.getNewEmail())) {
			teacherToUpdate.setTeacherEmail(teacher.getNewEmail());
			otherDetailsChanged = true;
		}

		// Only save if courses or other details have changed
		if (coursesChanged || otherDetailsChanged) {
			return teacherRepository.save(teacherToUpdate);
		} else {
			return teacherToUpdate; // Return the teacher without saving if no changes
		}
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

	public Teacher getTeacher(String teacherEmail) {
		return teacherRepository.findTeacherByTeacherEmail(teacherEmail);
	}

	public List<TeacherBatchProgramCourseDTO> getBatchProgramCourseInfoByTeacherId(Integer teacherId) {
		return teacherRepository.findBatchProgramCourseInfoByTeacherId(teacherId);
	}
}