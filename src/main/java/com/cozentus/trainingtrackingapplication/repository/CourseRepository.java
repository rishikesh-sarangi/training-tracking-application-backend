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

	@Query(value = "SELECT DISTINCT c.course_id, c.course_name, c.code, c.description, c.theory_time, c.practice_time, "
			+ "t.topic_id, t.topic_name, t.topic_order, t.theory_time, t.practice_time "
			+ "FROM batch_program_course_teacher bpct " + "INNER JOIN Course c ON bpct.course_id = c.course_id "
			+ "LEFT JOIN Topic t ON c.course_id = t.course_id " + "WHERE bpct.batch_id = :batchId "
			+ "AND bpct.program_id = :programId " + "AND bpct.teacher_id = :teacherId "
			+ "ORDER BY c.course_id, t.topic_order", nativeQuery = true)
	List<Object[]> findCoursesWithTopicsByBatchProgramAndTeacher(@Param("batchId") Integer batchId,
			@Param("programId") Integer programId, @Param("teacherId") Integer teacherId);
}
