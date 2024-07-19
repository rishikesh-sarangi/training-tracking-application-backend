package com.cozentus.trainingtrackingapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicDTO {
    private Integer topicId;
    private String topicName;
    private Integer topicOrder;
    private Integer theoryTime;
    private Integer practiceTime;
}
