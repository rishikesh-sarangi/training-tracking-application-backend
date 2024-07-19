package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneOffset;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Integer attendanceId;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

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

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "topic_percentage_completed")
    private Double topicPercentageCompleted;
    
    
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
