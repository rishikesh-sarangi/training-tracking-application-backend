package com.cozentus.trainingtrackingapplication.dto;

import lombok.Data;

@Data
public class TopicAddDTO {
    private String topicName;
    private String content;
    private Integer theoryTime;
    private Integer practiceTime;
    private String summary;
}
