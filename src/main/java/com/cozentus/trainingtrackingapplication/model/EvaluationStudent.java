package com.cozentus.trainingtrackingapplication.model;

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
	private Integer evaluationStudentId;

	@Column(name = "total_marks")
	private Integer totalMarks;

	@Column(name = "marks_secured")
	private Integer marksSecured;

	@ManyToOne
	@JoinColumn(name = "evaluation_id")
	private Evaluation evaluation;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
}
