package com.cozentus.trainingtrackingapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    
    @NotNull
    @Column(name = "file_name")
    private String fileName;
    
    @NotNull
    @Column(name = "file_path")
    private String filePath;
    
    @NotNull
    @Column(name = "file_type")
    private String fileType;
  
}
