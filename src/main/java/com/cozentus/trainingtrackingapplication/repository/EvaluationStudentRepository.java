package com.cozentus.trainingtrackingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.EvaluationStudent;

public interface EvaluationStudentRepository extends JpaRepository<EvaluationStudent, Integer> {
	
}
