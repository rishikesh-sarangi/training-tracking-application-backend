package com.cozentus.trainingtrackingapplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cozentus.trainingtrackingapplication.model.MyUsers;

@Repository
public interface MyUserRepository extends JpaRepository<MyUsers, Integer> {

	Optional<MyUsers> findByUserEmail(String email);
}
