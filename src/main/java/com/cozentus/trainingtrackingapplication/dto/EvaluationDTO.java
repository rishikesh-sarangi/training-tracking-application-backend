package com.cozentus.trainingtrackingapplication.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cozentus.trainingtrackingapplication.model.Batch;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Program;
import com.cozentus.trainingtrackingapplication.model.Teacher;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class EvaluationDTO {

    private String evaluationName;
    private String evaluationType;
    private LocalDate evaluationDate;
    private Integer totalMarks;
    private LocalTime evaluationTime;
    private Batch batch;
    private Program program;
    private Course course;
    private Teacher teacher;
}
