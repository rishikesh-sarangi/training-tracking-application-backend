package com.cozentus.trainingtrackingapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cozentus.trainingtrackingapplication.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

//	get topic by courseId
	List<Topic> findByCourse_CourseId(Integer courseId);

//	get the maximum topicOrder based on a coruseId
	@Query("SELECT MAX(t.order) FROM Topic t WHERE t.course.courseId = :courseId")
	Integer findMaxTopicOrderByCourseId(@Param("courseId") Integer courseId);
	
//	check if a certain topicName exists by a courseId
    @Query("SELECT COUNT(t) > 0 FROM Topic t WHERE t.topicName = :topicName AND t.course.courseId = :courseId")
    boolean existsByTopicNameAndCourseId(@Param("topicName") String topicName, @Param("courseId") Integer courseId);
	
}
