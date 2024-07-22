package com.cozentus.trainingtrackingapplication.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cozentus.trainingtrackingapplication.dto.TopicAddDTO;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Topic;
import com.cozentus.trainingtrackingapplication.model.TableFiles;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.TopicRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TopicService {
	
	private static final String DUPLICATE = "This topic Name already exists under this course!";

	private TopicRepository topicRepository;

	private CourseRepository courseRepository;

	TopicService(TopicRepository topicRepository, CourseRepository courseRepository) {

		this.topicRepository = topicRepository;
		this.courseRepository = courseRepository;
	}

	public List<Topic> getAllTopics() {
		return topicRepository.findAll();
	}

	public List<Topic> findByCourseId(Integer courseId) {
		return topicRepository.findByCourse_CourseId(courseId);
	}

	public Topic addTopic(Integer courseId, TopicAddDTO topicDto) {

		// Find the course by ID
		Optional<Course> course = courseRepository.findById(courseId);

//		check if the topic name is duplicate
		if (topicRepository.existsByTopicNameAndCourseId(topicDto.getTopicName(), courseId)) {
			if (course.isPresent()) {
				throw new DataIntegrityViolationException(
						DUPLICATE);
			} else {
				throw new EntityNotFoundException("Could'nt find course !");
			}
		}

		if (course.isPresent()) {
			// Get the maximum topic order for the given course ID
			Integer maxOrder = topicRepository.findMaxTopicOrderByCourseId(courseId);

			// Create a new Topic entity
			Topic topic = new Topic();
			topic.setCourse(course.get());
			topic.setOrder(maxOrder + 1); // Set the topic order to maxOrder + 1
			topic.setTopicName(topicDto.getTopicName());
			topic.setContent(topicDto.getContent());
			topic.setTheoryTime(topicDto.getTheoryTime());
			topic.setPracticeTime(topicDto.getPracticeTime());
			topic.setSummary(topicDto.getSummary());

			// Save the new Topic entity
			return topicRepository.save(topic);
		}
		return null;
	}

	public Topic editTopic(Integer topicId, Topic topic) {
		Optional<Topic> updatedTopic = topicRepository.findById(topicId);
		if (updatedTopic.isPresent()) {

			Integer courseId = updatedTopic.get().getCourse().getCourseId();

//			check if the topic name is duplicate
			if (topicRepository.existsByTopicNameAndCourseId(topic.getTopicName(), courseId)) {
				throw new DataIntegrityViolationException(
						DUPLICATE);
			}

			Topic topicToUpdate = updatedTopic.get();
			topicToUpdate.setTopicName(topic.getTopicName());
			topicToUpdate.setContent(topic.getContent());
			topicToUpdate.setTheoryTime(topic.getTheoryTime());
			topicToUpdate.setPracticeTime(topic.getPracticeTime());
			topicToUpdate.setSummary(topic.getSummary());
			topicToUpdate.setOrder(topic.getOrder());
			return topicRepository.save(topicToUpdate);
		}
		return null;
	}

	public Boolean deleteTopic(Integer topicId) {
		Optional<Topic> topic = topicRepository.findById(topicId);
		if (topic.isPresent()) {
			topicRepository.deleteById(topicId);
			return true;
		}
		return false;
	}

	public Topic uploadFiles(Integer topicId, MultipartFile[] files) {
		Topic topic = topicRepository.findById(topicId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

		try {
			// Get the path to src/main/resources
			File resourcesDirectory = ResourceUtils.getFile("classpath:");
			String uploadDir = resourcesDirectory.getParentFile().getParentFile().getPath()
					+ "/src/main/resources/uploads/";
			Path uploadPath = Paths.get(uploadDir);
			Files.createDirectories(uploadPath);

			for (MultipartFile file : files) {
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				String fileType = file.getContentType();
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				TableFiles topicFile = new TableFiles();
				topicFile.setFileName(fileName);
				topicFile.setFilePath("/uploads/" + fileName);
				topicFile.setFileType(fileType);
				topicFile.setTopic(topic);

				topic.addFile(topicFile);
			}

			return topicRepository.save(topic);

		} catch (IOException e) {
			e.getStackTrace();
			return null;
		}
	}
}
