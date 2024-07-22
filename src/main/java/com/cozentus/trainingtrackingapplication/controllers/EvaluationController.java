package com.cozentus.trainingtrackingapplication.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseTeacherResponse;
import com.cozentus.trainingtrackingapplication.dto.EvaluationDTO;
import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.model.TableFiles;
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
			if (!evaluations.isEmpty()) {
				return ResponseUtil.buildSuccessResponse(evaluations);
			} else {
				return ResponseUtil.buildErrorResponse("No evaluations found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

	@PostMapping
	public ResponseEntity<Object> createEvaluation(@RequestBody EvaluationDTO evaluationDto) {
		try {
			Evaluation evaluation = evaluationService.convertToEntity(evaluationDto);
			Evaluation createdEvaluation = evaluationService.saveEvaluation(evaluation);
			if (createdEvaluation != null) {
				return ResponseUtil.buildSuccessResponse(Collections.singletonList(createdEvaluation));
			} else {
				return ResponseUtil.buildErrorResponse("Error creating evaluation", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

//	this is a get request i'm using post mapping for filtering
	@PostMapping("/filter")
	public ResponseEntity<Object> getEvaluationsByFilters(@RequestBody BatchProgramCourseTeacherResponse filterDTO) {
		try {
			List<Evaluation> evaluations = evaluationService.getEvaluationsByTeacherBatchProgramAndCourse(
					filterDTO.getTeacherId(), filterDTO.getBatchId(), filterDTO.getProgramId(),
					filterDTO.getCourseId());

			if (!evaluations.isEmpty()) {
				return ResponseUtil.buildSuccessResponse(evaluations);
			} else {
				return ResponseUtil.buildErrorResponse("Error fetching evaluations:", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

	@DeleteMapping("/{evaluationId}")
	public ResponseEntity<Object> deleteAllEvaluations(@PathVariable Integer evaluationId) {
		try {
			Boolean response = evaluationService.deleteEvaluation(evaluationId);

			if (response.equals(true)) {
				return ResponseUtil.buildSuccessResponse(Collections.emptyList());
			} else {
				return ResponseUtil.buildErrorResponse("Evaluation not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

	@PatchMapping("/{evaluationId}")
	public ResponseEntity<Object> updateEvaluation(@PathVariable Integer evaluationId,
			@RequestBody EvaluationDTO evaluation) {
		try {
			Evaluation updatedEvaluation = evaluationService.editEvaluation(evaluationId, evaluation);
			if (updatedEvaluation != null) {
				return ResponseUtil.buildSuccessResponse(Collections.singletonList(updatedEvaluation));
			} else {
				return ResponseUtil.buildErrorResponse("Evaluation not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}
	
    @PostMapping("/{evaluationId}/upload")
    public ResponseEntity<Object> uploadFile(@PathVariable Integer evaluationId, @RequestParam("file") MultipartFile file) {
        try {
            TableFiles savedFile = evaluationService.saveFile(evaluationId, file);
            if(savedFile != null) {
            	return ResponseUtil.buildSuccessResponse(Collections.emptyList());
            }
            else {
            	return ResponseUtil.buildErrorResponse("File upload failed", HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
        	return ResponseUtil.buildErrorResponse("File Already Exists", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/file")
    public ResponseEntity<Object> doesFileExist(@RequestParam("fileName") String fileName) {
        try {
            Boolean savedFile = evaluationService.doesFileExist(fileName);
            if(savedFile.equals(true)) {
            	return ResponseUtil.buildErrorResponse("File Already Exists", HttpStatus.NOT_FOUND);
            }
            else {
            	return ResponseUtil.buildSuccessResponse(Collections.emptyList());
            }
        } catch (Exception e) {
        	return ResponseUtil.buildGenericErrorResponse();
        }
    }
}
