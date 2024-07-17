package com.cozentus.trainingtrackingapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Integer>{
	
//	get topic by courseId
	List<Topic> findByCourse_CourseId(Integer courseId);
}
