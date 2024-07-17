package com.cozentus.trainingtrackingapplication.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseWithTeachersDTO {
	private Integer courseId;
    private String courseName;
    private String courseCode;
    private List<TeacherDTO> teachers;
}
