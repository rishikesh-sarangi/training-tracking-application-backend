package com.cozentus.trainingtrackingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseProgressDTO {
    private Integer completedTopics;
    private Integer inProgressTopics;
    private Double courseCompletionPercentage;
}
