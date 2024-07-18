package com.cozentus.trainingtrackingapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluation_student")
public class EvaluationStudent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluation_student_id")
	private Integer evaluationStudentId;

	@Column(name = "marks_secured")
	private Integer marksSecured;
	
	@ManyToOne
	@JoinColumn(name = "evaluation_id")
	@JsonIgnoreProperties({"batch", "program","course","teacher"})
	private Evaluation evaluation;

	@ManyToOne
	@JoinColumn(name = "student_id")
	@JsonIgnoreProperties("batch")
	private Student student;
}
