package com.cozentus.trainingtrackingapplication.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cozentus.trainingtrackingapplication.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>{
	
	@Query("SELECT s FROM Student s WHERE s.batch.batchId = :batchId AND :programId IN (SELECT p.programId FROM s.programs p)")
    Set<Student> findStudentsByBatchIdAndProgramId(@Param("batchId") Integer batchId, @Param("programId") Integer programId);

	boolean existsByStudentCode(String studentCode);
}
