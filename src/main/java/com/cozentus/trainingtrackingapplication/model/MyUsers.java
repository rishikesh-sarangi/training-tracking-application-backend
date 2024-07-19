package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
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

	@Column(name = "created_date", updatable = false)
	@JsonIgnore
	private LocalDate createdDate;

	@Column(name = "updated_date")
	@JsonIgnore
	private LocalDate updatedDate;

	@Column(name = "created_by")
	@JsonIgnore
	private String createdBy = "System";

	@Column(name = "updated_by")
	@JsonIgnore
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