package com.cozentus.trainingtrackingapplication.dto;

import java.time.LocalDate;

public interface TeacherBatchProgramCourseDTO {
	Integer getBatchId();

	String getBatchCode();

	String getBatchName();

	LocalDate getBatchStartDate();

	Integer getProgramId();

	String getProgramCode();

	String getProgramName();

	Integer getCourseId();

	String getCourseCode();

	String getCourseName();
}
