package com.cozentus.trainingtrackingapplication.model;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Topic")
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "topic_id")
	private Integer topicId;

	@NotNull
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	@NotNull
	@Column(name = "topic_order")
	private Integer order;

	@NotNull
	@Column(name = "topic_name")
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
	@Column(name = "summary")
	private String summary;

	@JsonManagedReference(value = "topicFiles")
	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TableFiles> files = new ArrayList<>();

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

	@JsonIgnore
	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attendance> attendance;

	public void addFile(TableFiles file) {
		files.add(file);
		file.setTopic(this);
	}

	public void removeFile(TableFiles file) {
		files.remove(file);
		file.setTopic(null);
	}

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
