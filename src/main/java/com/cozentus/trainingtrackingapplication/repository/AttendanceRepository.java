package com.cozentus.trainingtrackingapplication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cozentus.trainingtrackingapplication.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
	@Query("SELECT a FROM Attendance a WHERE a.teacher.teacherId = :teacherId AND a.batch.batchId = :batchId "
			+ "AND a.program.programId = :programId AND a.course.courseId = :courseId AND a.attendanceDate = :attendanceDate")
	List<Attendance> findAttendance(@Param("teacherId") Integer teacherId, @Param("batchId") Integer batchId,
			@Param("programId") Integer programId, @Param("courseId") Integer courseId,
			@Param("attendanceDate") LocalDate attendanceDate);

	@Query("SELECT a FROM Attendance a WHERE a.batch.batchId = :batchId AND a.program.programId = :programId AND a.teacher.teacherId = :teacherId")
	List<Attendance> findByBatchIdAndProgramIdAndTeacherId(@Param("batchId") Integer batchId,
			@Param("programId") Integer programId, @Param("teacherId") Integer teacherId);
}
