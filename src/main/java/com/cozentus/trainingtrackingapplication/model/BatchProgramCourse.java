package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "batch_program_course")
public class BatchProgramCourse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer batchProgramCourseId;
	
	@NotNull
	private String courseName;
	
	@NotNull
	private Integer topicsCompleted;
	
	@NotNull
	private Integer topicsInProgress;
	
	@NotNull
	private Double courseCompletionPercentage;

	@ManyToOne
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@ManyToOne
	@JoinColumn(name = "program_id")
	private Program program;

	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;
}
