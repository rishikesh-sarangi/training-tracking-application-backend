 package com.cozentus.trainingtrackingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchProgramCourseTeacherResponse {
	private Integer batchId;
	private Integer programId;
	private Integer courseId;
	private Integer teacherId;
}
