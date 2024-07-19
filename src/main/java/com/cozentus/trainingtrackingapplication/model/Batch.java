package com.cozentus.trainingtrackingapplication.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Batch")
public class Batch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "batch_id")
	private Integer batchId;

	@NotNull
	@Column(name = "batch_name")
	private String batchName;

	@NotNull
	@Column(name = "batch_code")
	private String batchCode;

	@NotNull
	@Column(name = "start_date")
	private LocalDate batchStartDate;
	

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	@JsonIgnore
	private LocalDateTime createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	@JsonIgnore
	private LocalDateTime updatedDate;

	@Column(name = "created_by")
	@JsonIgnore
	private String createdBy = "System";

	@Column(name = "updated_by")
	@JsonIgnore
	private String updatedBy = "System";
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="batch_program", 
	joinColumns = @JoinColumn(name = "batch_id"),
	inverseJoinColumns = @JoinColumn(name = "program_id"))
	private Set<Program> programs;
	
	
//	@JsonManagedReference
	@JsonIgnore
	@OneToMany(mappedBy = "batch")
    private Set<Student> students;
	
	@JsonIgnore
	@OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
	private List<Evaluation> evaluations;
	
	@JsonIgnore
	@OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
	private List<Attendance> attendance;
	
	@JsonIgnore
	@OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
	private List<BatchProgramCourse> batchProgramCourse;
}
