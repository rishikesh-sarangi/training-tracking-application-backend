package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_id")
	private Integer courseId;

	@NotNull
	@Column(name = "course_name")
	private String courseName;

	@NotNull
	@Column(name = "code")
	private String code;

	@NotNull
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@NotNull
	@Column(name = "theory_time")
	private Integer theoryTime;

	@NotNull
	@Column(name = "practice_time")
	private Integer practiceTime;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	@JsonIgnore
	private LocalDate createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	@JsonIgnore
	private LocalDate updatedDate;

	@Column(name = "created_by")
	@JsonIgnore
	private String createdBy = "System";

	@Column(name = "updated_by")
	@JsonIgnore
	private String updatedBy = "System";

	@JsonManagedReference
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("files")
	private List<Topic> topics;

	@JsonIgnore
	@ManyToMany(mappedBy = "courses")
	private Set<Program> program;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "courses")
	private Set<Teacher> teachers;	
	
	@JsonIgnore
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Evaluation> evaluations;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Attendance> attendance;
	
	@JsonIgnore
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<BatchProgramCourse> batchProgramCourse;

}