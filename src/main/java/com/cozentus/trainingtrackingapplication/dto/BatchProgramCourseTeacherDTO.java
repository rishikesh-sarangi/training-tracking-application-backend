package com.cozentus.trainingtrackingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchProgramCourseTeacherDTO {
	private Integer courseId;
	private String courseName;
	private String courseCode;
	private Integer teacherId;
	private String teacherName;
}
