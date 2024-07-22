package com.cozentus.trainingtrackingapplication;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.cozentus.trainingtrackingapplication.model.Evaluation;
import com.cozentus.trainingtrackingapplication.repository.EvaluationRepository;
import com.cozentus.trainingtrackingapplication.service.EvaluationService;

@SpringBootTest
class EvaluationTesting {

    @InjectMocks
    private EvaluationService evaluationService;

    @Mock
    private EvaluationRepository evaluationRepository;

    private Evaluation evaluation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        evaluation = new Evaluation();
        evaluation.setEvaluationId(1);
        evaluation.setEvaluationName("Midterm Exam");
        evaluation.setEvaluationType("Exam");
        evaluation.setTotalMarks(100);
    }

    @Test
    void testSaveEvaluation() {
        when(evaluationRepository.save(any(Evaluation.class))).thenReturn(evaluation);
        
        Evaluation savedEvaluation = evaluationService.saveEvaluation(evaluation);
        
        assertNotNull(savedEvaluation);
        assertEquals("Midterm Exam", savedEvaluation.getEvaluationName());
    }

    @Test
    void testGetAllEvaluations() {
        when(evaluationRepository.findAll()).thenReturn(Arrays.asList(evaluation));
        
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        
        assertNotNull(evaluations);
        assertEquals(1, evaluations.size());
        assertEquals("Midterm Exam", evaluations.get(0).getEvaluationName());
    }

    @Test
    void testGetEvaluationsByTeacherBatchProgramAndCourse() {
        when(evaluationRepository.findByTeacherTeacherIdAndBatchBatchIdAndProgramProgramIdAndCourseCourseId(
            any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class)))
            .thenReturn(Arrays.asList(evaluation));
        
        List<Evaluation> evaluations = evaluationService.getEvaluationsByTeacherBatchProgramAndCourse(1, 1, 1, 1);
        
        assertNotNull(evaluations);
        assertEquals(1, evaluations.size());
        assertEquals("Midterm Exam", evaluations.get(0).getEvaluationName());
    }
    
    @Test
    void testGetAllEvaluationsReturnsEmptyList() {
        when(evaluationRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        
        assertNotNull(evaluations);
        assertTrue(evaluations.isEmpty());
    }

    @Test
    void testSaveEvaluationThrowsException() {
        when(evaluationRepository.save(any(Evaluation.class))).thenThrow(new RuntimeException("Error saving evaluation"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            evaluationService.saveEvaluation(evaluation);
        });
        
        assertEquals("Error saving evaluation", exception.getMessage());
    }

    @Test
    void testGetEvaluationsByTeacherBatchProgramAndCourseReturnsEmptyList() {
        when(evaluationRepository.findByTeacherTeacherIdAndBatchBatchIdAndProgramProgramIdAndCourseCourseId(
            any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class)))
            .thenReturn(Collections.emptyList());
        
        List<Evaluation> evaluations = evaluationService.getEvaluationsByTeacherBatchProgramAndCourse(1, 1, 1, 1);
        
        assertNotNull(evaluations);
        assertTrue(evaluations.isEmpty());
    }

    @Test
    void testRepositoryInteraction() {
        when(evaluationRepository.findByTeacherTeacherIdAndBatchBatchIdAndProgramProgramIdAndCourseCourseId(
            any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class)))
            .thenReturn(Arrays.asList(evaluation));
        
        evaluationService.getEvaluationsByTeacherBatchProgramAndCourse(1, 1, 1, 1);
        
        verify(evaluationRepository, times(1))
            .findByTeacherTeacherIdAndBatchBatchIdAndProgramProgramIdAndCourseCourseId(1, 1, 1, 1);
    }
}