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
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@Entity
@Table(name = "Evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Integer evaluationId;

    @Column(name = "evaluation_name")
    private String evaluationName;

    @Column(name = "evaluation_type")
    private String evaluationType;

    @Column(name = "eval_date")
    private LocalDate evaluationDate;
    
    @Column(name = "total_marks")
	private Integer totalMarks;

    @Column(name = "eval_time")
    private LocalTime evaluationTime;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "program_id")
    @JsonIgnoreProperties(value = "courses")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties(value = "topics")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties(value = "courses")
    private Teacher teacher;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;
    	
    @JsonIgnore
    @UpdateTimestamp
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
}