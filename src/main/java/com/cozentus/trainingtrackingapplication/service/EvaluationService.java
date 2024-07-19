package com.cozentus.trainingtrackingapplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.EvaluationDTO;
import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.repository.EvaluationRepository;

@Service
public class EvaluationService {

	private EvaluationRepository evaluationRepository;

	EvaluationService(EvaluationRepository evaluationRepository) {

		this.evaluationRepository = evaluationRepository;
	}

	public Evaluation saveEvaluation(Evaluation evaluation) {
		return evaluationRepository.save(evaluation);
	}

	public List<Evaluation> getAllEvaluations() {
		return evaluationRepository.findAll();
	}

	public List<Evaluation> getEvaluationsByTeacherBatchProgramAndCourse(Integer teacherId, Integer batchId,
			Integer programId, Integer courseId) {
		return evaluationRepository.findByTeacherTeacherIdAndBatchBatchIdAndProgramProgramIdAndCourseCourseId(teacherId,
				batchId, programId, courseId);
	}

	public Boolean deleteEvaluation(Integer evaluationId) {
		Optional<Evaluation> evaluation = evaluationRepository.findById(evaluationId);
		if (evaluation.isPresent()) {
			evaluationRepository.deleteById(evaluationId);
			return true;
		}
		return false;
	}

	public Evaluation editEvaluation(Integer evaluationId, EvaluationDTO evaluation) {
		Optional<Evaluation> updatedEvaluation = evaluationRepository.findById(evaluationId);
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
