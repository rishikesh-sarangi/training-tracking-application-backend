package com.cozentus.trainingtrackingapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
//	get evaluation by teacher id
	List<Evaluation> findByTeacherTeacherIdAndBatchBatchIdAndProgramProgramIdAndCourseCourseId(
	        Integer teacherId, Integer batchId, Integer programId, Integer courseId);
}
