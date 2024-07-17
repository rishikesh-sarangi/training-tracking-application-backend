package com.cozentus.trainingtrackingapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cozentus.trainingtrackingapplication.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
	@Query(value = "SELECT c.course_id, t.* FROM Course c "
			+ "INNER JOIN teacher_course tc ON c.course_id = tc.course_id "
			+ "INNER JOIN Teacher t ON tc.teacher_id = t.teacher_id "
			+ "WHERE c.course_id IN :courseIds", nativeQuery = true)
	List<Object[]> findTeachersByCourseIds(@Param("courseIds") List<Integer> courseIds);
}
