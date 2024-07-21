package com.cozentus.trainingtrackingapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "evaluation_student")
public class EvaluationStudent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluation_student_id")
	private Integer evaluationStudentId;
	
	@NotNull
	@Column(name = "marks_secured")
	private Integer marksSecured;
		
	@NotNull
	@ManyToOne
	@JoinColumn(name = "evaluation_id")
	@JsonIgnoreProperties({"batch", "program","course","teacher"})
	private Evaluation evaluation;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "student_id")
	@JsonIgnoreProperties("batch")
	private Student student;
}
