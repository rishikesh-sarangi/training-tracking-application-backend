package com.cozentus.trainingtrackingapplication.dto;

import java.util.List;

import com.cozentus.trainingtrackingapplication.model.Student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Integer batchId;
    private Integer programId;
    private Integer oldProgramId;
    private List<Student> students;
}
