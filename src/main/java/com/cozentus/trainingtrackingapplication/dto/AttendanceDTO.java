package com.cozentus.trainingtrackingapplication.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendanceDTO {
	private Integer batchId;
	private Integer programId;
	private Integer courseId;
	private Integer teacherId;
	private LocalDate attendanceDate;
}
