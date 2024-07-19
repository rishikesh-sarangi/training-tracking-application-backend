package com.cozentus.trainingtrackingapplication.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.AttendanceDTO;
import com.cozentus.trainingtrackingapplication.dto.AttendanceEditDTO;
import com.cozentus.trainingtrackingapplication.model.Attendance;
import com.cozentus.trainingtrackingapplication.model.Topic;
import com.cozentus.trainingtrackingapplication.repository.AttendanceRepository;
import com.cozentus.trainingtrackingapplication.repository.TopicRepository;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private TopicRepository topicRepository;
	

	public List<Attendance> getAllAttendances() {
		return attendanceRepository.findAll();
	}

	public Attendance addAttendance(Attendance attendance) {
		 return attendanceRepository.save(attendance);
	}

	public List<Attendance> getAttendanceByTeacherBatchProgramAndCourse(Integer teacherId, Integer batchId,
			Integer programId, Integer courseId, LocalDate attendanceDate) {
		return attendanceRepository.findAttendance(teacherId, batchId, programId, courseId, attendanceDate);
	}

	public boolean deleteAttendance(Integer attendanceId) {
		Optional<Attendance> attendance = attendanceRepository.findById(attendanceId);
		if (attendance.isPresent()) {
			attendanceRepository.deleteById(attendanceId);
			return true;
		}
		return false;
	}

	public Attendance editAttendance(Integer attendanceId, AttendanceEditDTO attendanceEditDTO) {
		Attendance attendanceToBeEdited = attendanceRepository.findById(attendanceId)
				.orElseThrow(() -> new IllegalArgumentException("Attendance not found with id: " + attendanceId));

		Optional<Topic> topic = topicRepository.findById(attendanceEditDTO.getTopicId());
		if (topic.isPresent()) {
			attendanceToBeEdited.setTopic(topic.get());
		} else {
			throw new IllegalArgumentException("Topic not found with id: " + attendanceEditDTO.getTopicId());
		}
		attendanceToBeEdited.setTopicName(attendanceEditDTO.getTopicName());
		attendanceToBeEdited.setTopicPercentageCompleted(attendanceEditDTO.getTopicPercentageCompleted());

		return attendanceRepository.save(attendanceToBeEdited);
	}
	
	public List<Attendance> findByBatchIdAndProgramIdAndTeacherId(AttendanceDTO filterDTO) {
		Integer batchId = filterDTO.getBatchId();
		Integer programId = filterDTO.getProgramId();
		Integer teacherId = filterDTO.getTeacherId();
		return attendanceRepository.findByBatchIdAndProgramIdAndTeacherId(batchId, programId, teacherId);
	}
}
