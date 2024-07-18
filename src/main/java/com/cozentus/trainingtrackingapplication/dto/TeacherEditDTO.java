package com.cozentus.trainingtrackingapplication.dto;

import java.util.List;

import com.cozentus.trainingtrackingapplication.model.Course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherEditDTO {
	private String teacherName;
	private List<Course> courses;
	private String oldEmail;
	private String newEmail;
}
