package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cozentus.trainingtrackingapplication.model.Topic;
import com.cozentus.trainingtrackingapplication.service.TopicService;

@RestController
@RequestMapping("/topics")
@CrossOrigin("http://localhost:4200/")
public class TopicController {

	private TopicService topicService;

	TopicController(TopicService topicService) {
		this.topicService = topicService;
	}

	@GetMapping
	public ResponseEntity<List<Topic>> getAllTopics() {
		Optional<List<Topic>> topics = Optional.ofNullable(topicService.getAllTopics());
		if (topics.isPresent()) {
			return new ResponseEntity<>(topics.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{courseId}")
	public ResponseEntity<List<Topic>> getTopicsByCourseId(@PathVariable Integer courseId) {
		Optional<List<Topic>> topics = Optional.ofNullable(topicService.findByCourseId(courseId));
		if (topics.isPresent()) {
			return new ResponseEntity<>(topics.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/{courseId}")
	public ResponseEntity<Topic> createTopic(@PathVariable Integer courseId, @RequestBody Topic topic) {
		Optional<Topic> createdTopic = Optional.ofNullable(topicService.addTopic(courseId, topic));
		if (createdTopic.isPresent()) {
			return new ResponseEntity<>(createdTopic.get(), HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{topicId}")
	public ResponseEntity<Boolean> deleteTopic(@PathVariable Integer topicId) {
		Optional<Boolean> deletedTopic = Optional.of(topicService.deleteTopic(topicId));
		if (deletedTopic.isPresent()) {
			return new ResponseEntity<>(deletedTopic.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{topicId}")
	public ResponseEntity<Topic> editTopic(@PathVariable Integer topicId, @RequestBody Topic topic) {
		try {
			Optional<Topic> updatedTopic = Optional.of(topicService.editTopic(topicId, topic));
			if (updatedTopic.get() != null) {
				return new ResponseEntity<>(updatedTopic.get(), HttpStatus.OK);
			}
			else {				
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/upload/{topicId}", consumes = "multipart/form-data")
	public Topic postMethodName(@PathVariable Integer topicId, @RequestParam MultipartFile[] files) {
		return topicService.uploadFiles(topicId, files);
	}

}
