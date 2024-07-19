package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@NoArgsConstructor
@Entity
@Table(name = "Evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Integer evaluationId;

    @NotNull
    @Column(name = "evaluation_name")
    private String evaluationName;

    
    @NotNull
    @Column(name = "evaluation_type")
    private String evaluationType;

    @NotNull
    @Column(name = "eval_date")
    private LocalDate evaluationDate;
    
    @NotNull
    @Column(name = "total_marks")
	private Integer totalMarks;

    @NotNull
    @Column(name = "eval_time")
    private LocalTime evaluationTime;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "program_id")
    @JsonIgnoreProperties(value = "courses")
    private Program program;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties(value = "topics")
    private Course course;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties(value = "courses")
    private Teacher teacher;

    @JsonIgnore
    @Column(name = "created_date")
    private LocalDate createdDate;
    	
    @JsonIgnore
    @Column(name = "updated_date")
    private LocalDate updatedDate;
    
    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy = "System";

    @JsonIgnore
    @Column(name = "updated_by")
    private String updatedBy = "System";
    
    @JsonIgnore
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EvaluationStudent> evaluationStudents;
    
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