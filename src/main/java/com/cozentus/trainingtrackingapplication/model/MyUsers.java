package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class MyUsers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@NotNull
	@Column(name = "user_email")
	private String userEmail;
	
	@NotNull
	@Column(name = "username")
	private String username;

	@NotNull
	@Column(name = "user_password")
	private String userPassword;

	@NotNull
	@Column(name = "user_role")
	private String userRole;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	@JsonIgnore
	private LocalDate createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	@JsonIgnore
	private LocalDate updatedDate;

	@Column(name = "created_by")
	@JsonIgnore
	private String createdBy = "System";

	@Column(name = "updated_by")
	@JsonIgnore
	private String updatedBy = "System";
}