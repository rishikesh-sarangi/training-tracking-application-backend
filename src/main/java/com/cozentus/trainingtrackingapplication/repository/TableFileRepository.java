package com.cozentus.trainingtrackingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.model.TableFiles;

public interface TableFileRepository extends JpaRepository<TableFiles, Integer> {
	boolean existsByFileNameAndEvaluation(String fileName, Evaluation evaluation);
	boolean existsByFileName(String fileName);
}
