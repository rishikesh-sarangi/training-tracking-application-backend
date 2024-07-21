package com.cozentus.trainingtrackingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cozentus.trainingtrackingapplication.model.EvaluationStudent;

public interface EvaluationStudentRepository extends JpaRepository<EvaluationStudent, Integer> {
	@Modifying
	@Query("DELETE FROM EvaluationStudent es WHERE es.evaluation.id IN (SELECT e.id FROM Evaluation e WHERE e.batch.id = :batchId)")
	void deleteAllByEvaluationId(@Param("batchId") Integer batchId);
}
