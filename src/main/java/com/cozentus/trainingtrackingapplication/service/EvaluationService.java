package com.cozentus.trainingtrackingapplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.EvaluationDTO;
import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.repository.EvaluationRepository;

import jakarta.persistence.EntityNotFoundException;

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
    
    public Evaluation editEvaluation(Integer evaluationId, EvaluationDTO evaluation) {
    	Optional<Evaluation> updatedEvaluation = Optional.ofNullable(evaluationRepository.findById(evaluationId)
				.orElseThrow(() -> new EntityNotFoundException("Evaluation not found")));
		if (updatedEvaluation.isPresent()) {
			Evaluation evaluationToBeUpdated = updatedEvaluation.get();
			evaluationToBeUpdated.setEvaluationName(evaluation.getEvaluationName());
			evaluationToBeUpdated.setTotalMarks(evaluation.getTotalMarks());
			evaluationToBeUpdated.setEvaluationDate(evaluation.getEvaluationDate());
			evaluationToBeUpdated.setEvaluationTime(evaluation.getEvaluationTime());
			return evaluationRepository.save(evaluationToBeUpdated);
		}
		return null;
	}
}
