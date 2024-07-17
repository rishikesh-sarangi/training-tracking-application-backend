package com.cozentus.trainingtrackingapplication.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentUpdateDTO {

	private Integer batchId;
	private Integer currentProgramId;
	private Integer newProgramId; 
	private List<Integer> newStudentIds; 

}
