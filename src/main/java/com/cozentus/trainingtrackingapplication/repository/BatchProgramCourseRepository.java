package com.cozentus.trainingtrackingapplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.Batch;
import com.cozentus.trainingtrackingapplication.model.BatchProgramCourse;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Program;

public interface BatchProgramCourseRepository extends JpaRepository<BatchProgramCourse, Integer> {
	 Optional<BatchProgramCourse> findByBatchAndProgramAndCourse(Batch batch, Program program, Course course);
}
