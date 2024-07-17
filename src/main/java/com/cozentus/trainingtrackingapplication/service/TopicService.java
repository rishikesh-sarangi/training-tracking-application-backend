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
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Topic;
import com.cozentus.trainingtrackingapplication.model.TableFiles;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.TopicRepository;


@Service
public class TopicService {

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private CourseRepository courseRepository;

	public List<Topic> getAllTopics() {
		return topicRepository.findAll();
	}

	public List<Topic> findByCourseId(Integer courseId) {
		return topicRepository.findByCourse_CourseId(courseId);
	}

	public Topic addTopic(Integer courseId, Topic newTopic) {
		Optional<Course> course = courseRepository.findById(courseId);

		if (course.isPresent()) {
			newTopic.setCourse(course.get());
			return topicRepository.save(newTopic);
		}
		return null;
	}

	public Topic editTopic(Integer topicId, Topic topic) {
		Optional<Topic> updatedTopic = topicRepository.findById(topicId);
		if (updatedTopic.isPresent()) {
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
		return null;
	}

	public Topic uploadFiles(Integer topicId, MultipartFile[] files) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        try {
            // Get the path to src/main/resources
            File resourcesDirectory = ResourceUtils.getFile("classpath:");
            String uploadDir = resourcesDirectory.getParentFile().getParentFile().getPath() + "/src/main/resources/uploads/";
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

           return  topicRepository.save(topic);

        } catch (IOException e) {
        	e.getStackTrace();
           return null;
        }
	}
}
