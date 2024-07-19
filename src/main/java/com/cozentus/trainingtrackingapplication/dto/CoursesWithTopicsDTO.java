package com.cozentus.trainingtrackingapplication.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoursesWithTopicsDTO {
    private Integer courseId;
    private String courseName;
    private String code;
    private String description;
    private Integer theoryTime;
    private Integer practiceTime;
    private List<TopicDTO> topics;
}
