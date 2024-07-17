package com.cozentus.trainingtrackingapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
	@Column(name = "user_email", unique = true, length = 50)
	private String userEmail;
	
	@NotNull
	@Column(name = "username", unique = true, length = 50)
	private String username;

	@NotNull
	@Column(name = "user_password", length = 20)
	private String userPassword;

	@NotNull
	@Column(name = "user_role", length = 30)
	private String userRole;

	@NotNull
	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDate createdDate;

	@NotNull
	@UpdateTimestamp
	@Column(name = "updated_date")
	private LocalDate updatedDate;

	@NotNull
	@Column(name = "created_by", length = 50)
	private String createdBy;

	@NotNull
	@Column(name = "updated_by", length = 50)
	private String updatedBy;

}