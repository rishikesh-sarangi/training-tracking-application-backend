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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Program")
public class Program {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "program_id")
	private Integer programId;

	@NotNull
	@Column(name = "program_name")
	private String programName;

	@NotNull
	@Column(name = "program_code")
	private String programCode;

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
	
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "Program_Course",
        joinColumns = @JoinColumn(name = "program_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnoreProperties("topics")
	private List<Course> courses;
	
	@JsonIgnore
    @ManyToMany(mappedBy = "programs")
    private Set<Batch> batches;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "programs")
    private Set<Student> students;
    
    @JsonIgnore
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<Evaluation> evaluations;
}