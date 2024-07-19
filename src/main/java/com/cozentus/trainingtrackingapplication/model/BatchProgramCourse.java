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
@Table(name = "batch_program_course")
public class BatchProgramCourse {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer batchProgramCourseId;
    
    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;
    
    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    @Column(name = "course_name")
    private String courseName;
    
    @Column(name = "course_completion_percentage")
	private Double courseCompletionPercentage;
}
