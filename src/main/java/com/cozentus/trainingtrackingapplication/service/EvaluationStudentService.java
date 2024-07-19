package com.cozentus.trainingtrackingapplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.EvaluationStudentDTO;
import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.model.EvaluationStudent;
import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.repository.EvaluationRepository;
import com.cozentus.trainingtrackingapplication.repository.EvaluationStudentRepository;
import com.cozentus.trainingtrackingapplication.repository.StudentRepository;

@Service
public class EvaluationStudentService {

	private EvaluationStudentRepository evaluationStudentRepository;

	private EvaluationRepository evaluationRepository;

	private StudentRepository studentRepository;

	EvaluationStudentService(EvaluationStudentRepository evaluationStudentRepository,
			EvaluationRepository evaluationRepository, StudentRepository studentRepository) {

		this.evaluationStudentRepository = evaluationStudentRepository;
		this.evaluationRepository = evaluationRepository;
		this.studentRepository = studentRepository;
	}

	public EvaluationStudent addEvaluationStudent(EvaluationStudentDTO evaluationStudentDTO) {
		EvaluationStudent newEvaluationStudent = new EvaluationStudent();
		Evaluation evaluation = evaluationRepository.findById(evaluationStudentDTO.getEvaluationId())
				.orElseThrow(() -> new RuntimeException("Evaluation not found"));
		Student student = studentRepository.findById(evaluationStudentDTO.getStudentId())
				.orElseThrow(() -> new RuntimeException("Student not found"));

		newEvaluationStudent.setEvaluation(evaluation);
		newEvaluationStudent.setStudent(student);
		newEvaluationStudent.setMarksSecured(evaluationStudentDTO.getMarksSecured());
		return evaluationStudentRepository.save(newEvaluationStudent);
	}

	public List<EvaluationStudent> addMultipleEvaluationStudent(List<EvaluationStudent> evaluationStudent) {
		return evaluationStudentRepository.saveAll(evaluationStudent);
	}

}
