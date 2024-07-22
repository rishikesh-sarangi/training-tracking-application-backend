package com.cozentus.trainingtrackingapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cozentus.trainingtrackingapplication.dto.TeacherBatchProgramCourseDTO;
import com.cozentus.trainingtrackingapplication.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {


// find teacher by email
	@Query("SELECT t FROM Teacher t WHERE t.teacherEmail = :teacherEmail")
	Teacher findTeacherByTeacherEmail(@Param("teacherEmail") String teacherEmail);
	
	
	
	@Query(value = "SELECT DISTINCT " +
	           "b.batch_id as batchId, b.batch_code as batchCode, b.batch_name as batchName, b.start_date as batchStartDate, " +
	           "p.program_id as programId, p.program_code as programCode, p.program_name as programName, " +
	           "c.course_id as courseId, c.code as courseCode, c.course_name as courseName " +
	           "FROM batch_program_course_teacher bpct " +
	           "JOIN Batch b ON b.batch_id = bpct.batch_id " +
	           "JOIN Program p ON p.program_id = bpct.program_id " +
	           "JOIN Course c ON c.course_id = bpct.course_id " +
	           "WHERE bpct.teacher_id = :teacherId", 
	           nativeQuery = true)
	    List<TeacherBatchProgramCourseDTO> findBatchProgramCourseInfoByTeacherId(@Param("teacherId") Integer teacherId);
}
