package com.cozentus.trainingtrackingapplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cozentus.trainingtrackingapplication.model.Batch;

import jakarta.transaction.Transactional;

public interface BatchRepository extends JpaRepository<Batch, Integer> {
	@Query("SELECT b FROM Batch b LEFT JOIN FETCH b.programs p LEFT JOIN FETCH p.students WHERE b.batchId = :batchId")
	Optional<Batch> findByIdWithProgramsAndStudents(@Param("batchId") Integer batchId);

	@Query(value = "SELECT DISTINCT c.course_id, c.course_name, c.code, c.description, c.theory_time, c.practice_time "
			+ "FROM Batch b " + "INNER JOIN batch_program bp ON b.batch_id = bp.batch_id "
			+ "INNER JOIN Program p ON bp.program_id = p.program_id "
			+ "INNER JOIN Program_Course pc ON p.program_id = pc.program_id "
			+ "INNER JOIN Course c ON pc.course_id = c.course_id "
			+ "WHERE b.batch_id = :batchId AND p.program_id = :programId", nativeQuery = true)
	List<Object[]> findCoursesByBatchIdAndProgramId(@Param("batchId") Integer batchId,
			@Param("programId") Integer programId);

	@Query(value = "SELECT c.course_id AS courseId, c.course_name AS courseName, c.code AS courseCode, t.teacher_id AS teacherId, t.name AS teacherName "
			+ "FROM batch_program_course_teacher bpct " + "JOIN Course c ON bpct.course_id = c.course_id "
			+ "JOIN Teacher t ON bpct.teacher_id = t.teacher_id "
			+ "WHERE bpct.batch_id = :batchId AND bpct.program_id = :programId", nativeQuery = true)
	List<Object[]> findCourseAndTeacherByBatchAndProgram(@Param("batchId") Integer batchId,
			@Param("programId") Integer programId);

//	delete batch_program_course_teacher based on batchId
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM batch_program_course_teacher WHERE batch_id = :batchId", nativeQuery = true)
	void deleteByBatchId(@Param("batchId") Integer batchId);

//	delete batch_program_course_teacher based on batchId, programId	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM batch_program_course_teacher WHERE batch_id = :batchId AND program_id = :programId", nativeQuery = true)
	void deleteByBatchAndProgramId(@Param("batchId") Integer batchId, @Param("programId") Integer programId);

//	delete batch_program_course_teacher based on batchId, programId, courseId, teacherId
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM batch_program_course_teacher WHERE batch_id = :batchId AND program_id = :programId AND course_id = :courseId AND teacher_id = :teacherId", nativeQuery = true)
	void deleteByBatchIdAndProgramIdAndCourseIdAndTeacherId(@Param("batchId") Integer batchId,
			@Param("programId") Integer programId, @Param("courseId") Integer courseId,
			@Param("teacherId") Integer teacherId);

//	delete batch_program_course_teacher based on courseId
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM batch_program_course_teacher WHERE course_id = :courseId", nativeQuery = true)
	void deleteByCourseId(@Param("courseId") Integer courseId);

	
//	delete batch_program_course_teacher based on programId
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM batch_program_course_teacher WHERE program_id = :programId", nativeQuery = true)
	void deleteByProgramId(@Param("programId") Integer programId);
	
//	delete batch_program_course_teacher based on programId and courseId
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM batch_program_course_teacher WHERE program_id = :programId AND course_id = :courseId", nativeQuery = true)
	void deleteByProgramIdAndCourseId(@Param("programId") Integer programId, @Param("courseId") Integer courseId);

//	delete batch_program_course_teacher based on teacherId and coruseId
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM batch_program_course_teacher WHERE teacher_id = :teacherId AND course_id = :courseId", nativeQuery = true)
	void deleteByTeacherIdAndCourseId(@Param("teacherId") Integer teacherId, @Param("courseId") Integer courseId);
	
//	delete batch_program_course_teacher based on teacherId
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM batch_program_course_teacher WHERE teacher_id = :teacherId", nativeQuery = true)
	void deleteByTeacherId(@Param("teacherId") Integer teacherId);
	
	boolean existsByBatchCode(String batchCode);
	boolean existsByBatchName(String batchName);
}
