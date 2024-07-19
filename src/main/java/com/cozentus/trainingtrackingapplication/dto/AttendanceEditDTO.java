package com.cozentus.trainingtrackingapplication.dto;

import lombok.Data;

@Data
public class AttendanceEditDTO {
	Integer topicId;
	String topicName;
	Double topicPercentageCompleted;
}
