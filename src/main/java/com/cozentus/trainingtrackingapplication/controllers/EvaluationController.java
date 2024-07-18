package com.cozentus.trainingtrackingapplication.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.model.EvaluationFilterDTO;
import com.cozentus.trainingtrackingapplication.service.EvaluationService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/evaluations")
public class EvaluationController {

	private final EvaluationService evaluationService;

	public EvaluationController(EvaluationService evaluationService) {
		this.evaluationService = evaluationService;
	}
	
    @GetMapping
    public ResponseEntity<Object> getAllEvaluations() {
        try {
            List<Evaluation> evaluations = evaluationService.getAllEvaluations();
            return ResponseUtil.buildSuccessResponse(evaluations);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("Error fetching evaluations: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@PostMapping
	public ResponseEntity<Object> createEvaluation(@RequestBody Evaluation evaluation) {
		try {
			Evaluation createdEvaluation = evaluationService.saveEvaluation(evaluation);
			return ResponseUtil.buildSuccessResponse(Collections.singletonList(createdEvaluation));
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error creating evaluation: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	this is a get request i'm using post mapping for filtering
	@PostMapping("/filter")
    public ResponseEntity<Object> getEvaluationsByFilters(@RequestBody EvaluationFilterDTO filterDTO) {
        try {
            List<Evaluation> evaluations = evaluationService.getEvaluationsByTeacherBatchProgramAndCourse(
                filterDTO.getTeacherId(), 
                filterDTO.getBatchId(), 
                filterDTO.getProgramId(), 
                filterDTO.getCourseId()
            );
            return ResponseUtil.buildSuccessResponse(evaluations);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("Error fetching evaluations: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@DeleteMapping("/{evaluationId}")
	public ResponseEntity<Object> deleteAllEvaluations(@PathVariable Integer evaluationId) {
		try {
			evaluationService.deleteEvaluation(evaluationId);
			return ResponseUtil.buildSuccessResponse(Collections.emptyList());
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error deleting evaluations: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
