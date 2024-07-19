package com.cozentus.trainingtrackingapplication.model;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Integer studentId;

	@NotNull
	@Column(name = "name")
	private String studentName;

	@NotNull
	@Column(name = "email")
	private String studentEmail;

	@NotNull
	@Column(name = "student_code")
	private String studentCode;
	
	@Column(name = "created_date", updatable = false)
	@JsonIgnore
	private LocalDate createdDate;

	@Column(name = "updated_date")
	@JsonIgnore
	private LocalDate updatedDate;

	@Column(name = "created_by")
	@JsonIgnore
	private String createdBy = "System";

	@Column(name = "updated_by")
	@JsonIgnore
	private String updatedBy = "System";
	
//	@JsonBackReference
	@ManyToOne(optional = true)
    @JoinColumn(name = "batch_id", nullable = true)
    private Batch batch;
	
	@JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "program_student",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private Set<Program> programs;

	@JsonIgnore
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EvaluationStudent> evaluationStudents;
	
	@PrePersist
	protected void onCreate() {
		createdDate = LocalDate.now(ZoneOffset.UTC);
		updatedDate = LocalDate.now(ZoneOffset.UTC);
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDate = LocalDate.now(ZoneOffset.UTC);
	}
}
