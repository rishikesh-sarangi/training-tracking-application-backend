package com.cozentus.trainingtrackingapplication.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	
}
