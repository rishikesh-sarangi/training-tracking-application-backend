package com.cozentus.trainingtrackingapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
	  private Integer studentId;
      private String studentName;
      private String studentEmail;
      private String studentCode;
}
