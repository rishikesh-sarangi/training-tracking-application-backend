package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "created_by")
    private String createdBy = "System";

    @Column(name = "updated_by")
    private String updatedBy = "System";
}