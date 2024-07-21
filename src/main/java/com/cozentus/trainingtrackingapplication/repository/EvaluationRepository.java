package com.cozentus.trainingtrackingapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cozentus.trainingtrackingapplication.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
//	get evaluation by teacher id
	List<Evaluation> findByTeacherTeacherIdAndBatchBatchIdAndProgramProgramIdAndCourseCourseId(
	        Integer teacherId, Integer batchId, Integer programId, Integer courseId);
	

	
	@Modifying
	@Query("DELETE FROM Evaluation e WHERE e.batch.id = :batchId")
	void deleteAllByBatchId(@Param("batchId") Integer batchId);
}
