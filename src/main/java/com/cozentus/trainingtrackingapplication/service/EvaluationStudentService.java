package com.cozentus.trainingtrackingapplication.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private EvaluationStudentRepository evaluationStudentRepository;
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	

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
