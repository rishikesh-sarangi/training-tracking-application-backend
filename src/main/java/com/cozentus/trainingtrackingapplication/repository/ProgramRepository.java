package com.cozentus.trainingtrackingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.Program;

public interface ProgramRepository extends JpaRepository<Program, Integer> {
	boolean existsByProgramCode(String programCode);

	boolean existsByProgramName(String programName);
}
