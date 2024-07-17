package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Evaluation")
public class Evaluation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer evaluationId;
	
	@NotNull
	private String evaluationName;
	
	@NotNull
	private String evaluationType;
	
	@NotNull
	private LocalDate submissionDate;

	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	@ManyToOne
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@ManyToOne
	@JoinColumn(name = "program_id")
	private Program program;

	@OneToMany(mappedBy = "evaluation")
	private List<EvaluationStudent> evaluationStudents;
}
