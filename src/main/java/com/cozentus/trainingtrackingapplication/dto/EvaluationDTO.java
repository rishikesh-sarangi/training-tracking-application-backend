package com.cozentus.trainingtrackingapplication.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class EvaluationDTO {
	private LocalDate evaluationDate;
	private LocalTime evaluationTime;
	private Integer totalMarks;
	private String evaluationName;
}
