package com.cozentus.trainingtrackingapplication.model;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "Teacher")
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "teacher_id")
	private Integer teacherId;

	@NotNull
	@Column(name = "name")
	private String teacherName;

	@NotNull
	@Column(name = "email")
	private String teacherEmail;

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

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "teacher_course", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	@JsonIgnoreProperties("topics")
	private List<Course> courses = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
	private List<Evaluation> evaluations;
	
	@JsonIgnore
	@OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
	private List<Attendance> attendance;
	
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