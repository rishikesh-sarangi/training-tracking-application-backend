package com.cozentus.trainingtrackingapplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.repository.EvaluationRepository;

@Service
public class EvaluationService {
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	
	public Evaluation saveEvaluation(Evaluation evaluation) {
		return evaluationRepository.save(evaluation);
	}
	
	public List<Evaluation> getAllEvaluations() {
		return evaluationRepository.findAll();
	}
	
    public List<Evaluation> getEvaluationsByTeacherBatchProgramAndCourse(
            Integer teacherId, Integer batchId, Integer programId, Integer courseId) {
        return evaluationRepository.findByTeacherTeacherIdAndBatchBatchIdAndProgramProgramIdAndCourseCourseId(
            teacherId, batchId, programId, courseId);
    }
    
    public Boolean deleteEvaluation(Integer evaluationId) {
		evaluationRepository.deleteById(evaluationId);
		return true;
	}
}
