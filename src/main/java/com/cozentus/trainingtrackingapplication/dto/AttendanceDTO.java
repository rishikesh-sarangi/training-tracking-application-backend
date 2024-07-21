package com.cozentus.trainingtrackingapplication.dto;

import java.time.LocalDate;

import com.cozentus.trainingtrackingapplication.model.Batch;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Program;
import com.cozentus.trainingtrackingapplication.model.Teacher;
import com.cozentus.trainingtrackingapplication.model.Topic;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceDTO {
    private LocalDate attendanceDate;
    private Batch batch;
    private Program program;
    private Course course;
    private Teacher teacher;
    private Topic topic;
    private String topicName;
    private Double topicPercentageCompleted;
}
