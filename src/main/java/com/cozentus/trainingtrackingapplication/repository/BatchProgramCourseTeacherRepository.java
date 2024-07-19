package com.cozentus.trainingtrackingapplication.repository;


import org.springframework.stereotype.Repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class BatchProgramCourseTeacherRepository {
	
	@PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertBatchProgramCourseTeacher(Integer batchId, Integer programId ,Integer courseId, Integer teacherId) {
        String sql = "INSERT INTO batch_program_course_teacher (batch_id, program_id, course_id, teacher_id) VALUES (:batchId, :programId ,:courseId, :teacherId)";
        
        entityManager.createNativeQuery(sql)
            .setParameter("batchId", batchId)
            .setParameter("programId", programId)
            .setParameter("courseId", courseId)
            .setParameter("teacherId", teacherId)
            .executeUpdate();
    }
}
