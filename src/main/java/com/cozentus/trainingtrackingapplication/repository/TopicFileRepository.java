package com.cozentus.trainingtrackingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozentus.trainingtrackingapplication.model.TableFiles;

public interface TopicFileRepository extends JpaRepository<TableFiles, Integer> {

}
