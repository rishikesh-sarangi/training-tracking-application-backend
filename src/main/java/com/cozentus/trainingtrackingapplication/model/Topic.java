package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Integer topicId;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @NotNull
    @Column(name = "topic_order")
    private Integer order;

    @NotNull
    @Column(name = "topic_name", length = 255)
    private String topicName;

    @NotNull
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Column(name = "theory_time")
    private Integer theoryTime;

    @NotNull
    @Column(name = "practice_time")
    private Integer practiceTime;

    @NotNull
    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TableFiles> files = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
	@JsonIgnore
	private LocalDate createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	@JsonIgnore
	private LocalDate updatedDate;

	@Column(name = "created_by", length = 50)
	@JsonIgnore
	private String createdBy = "System";

	@Column(name = "updated_by", length = 50)
	@JsonIgnore
    private String updatedBy = "System";
	
    public void addFile(TableFiles file) {
        files.add(file);
        file.setTopic(this);
    }

    public void removeFile(TableFiles file) {
        files.remove(file);
        file.setTopic(null);
    }
}
