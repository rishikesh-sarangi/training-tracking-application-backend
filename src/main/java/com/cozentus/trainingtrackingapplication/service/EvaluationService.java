package com.cozentus.trainingtrackingapplication.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cozentus.trainingtrackingapplication.dto.EvaluationDTO;
import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.model.TableFiles;
import com.cozentus.trainingtrackingapplication.repository.EvaluationRepository;
import com.cozentus.trainingtrackingapplication.repository.TableFileRepository;

@Service
public class EvaluationService {

	private EvaluationRepository evaluationRepository;

	private TableFileRepository tableFileRepository;

	private final Path root = Paths.get("src", "main", "resources", "uploads");

	EvaluationService(EvaluationRepository evaluationRepository, TableFileRepository tableFileRepository) {
		this.tableFileRepository = tableFileRepository;
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

	public boolean doesFileExist(String fileName) {
		if (tableFileRepository.existsByFileName(fileName)) {
			return false;
		} else {
			return true;
		}

	}

	public TableFiles saveFile(Integer evaluationId, MultipartFile file) throws IOException {
		Evaluation evaluation = evaluationRepository.findById(evaluationId)
				.orElseThrow(() -> new RuntimeException("Evaluation not found"));

		String fileName = file.getOriginalFilename();
		String filePath = root.resolve(fileName).toString();
		String fileType = file.getContentType();

		// Check if the file already exists
		if (tableFileRepository.existsByFileNameAndEvaluation(fileName, evaluation)) {
			throw new RuntimeException("File with the same name already exists for this evaluation.");
		}

		Files.copy(file.getInputStream(), this.root.resolve(fileName));

		TableFiles evaluationFile = new TableFiles();
		evaluationFile.setEvaluation(evaluation);
		evaluationFile.setFileName(fileName);
		evaluationFile.setFilePath(filePath);
		evaluationFile.setFileType(fileType);

		return tableFileRepository.save(evaluationFile);
	}
	
//	DTO to entity converter
	public Evaluation convertToEntity(EvaluationDTO evaluationDTO) {
	    Evaluation evaluation = new Evaluation();
	    evaluation.setEvaluationName(evaluationDTO.getEvaluationName());
	    evaluation.setEvaluationType(evaluationDTO.getEvaluationType());
	    evaluation.setEvaluationDate(evaluationDTO.getEvaluationDate());
	    evaluation.setTotalMarks(evaluationDTO.getTotalMarks());
	    evaluation.setEvaluationTime(evaluationDTO.getEvaluationTime());
	    evaluation.setBatch(evaluationDTO.getBatch());
	    evaluation.setProgram(evaluationDTO.getProgram());
	    evaluation.setCourse(evaluationDTO.getCourse());
	    evaluation.setTeacher(evaluationDTO.getTeacher());
	    return evaluation;
	}

}
