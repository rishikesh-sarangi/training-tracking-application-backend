package com.cozentus.trainingtrackingapplication.model;

import lombok.Data;

@Data
public class EvaluationFilterDTO {
    private Integer teacherId;
    private Integer batchId;
    private Integer programId;
    private Integer courseId;
}