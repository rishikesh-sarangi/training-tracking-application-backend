package com.cozentus.trainingtrackingapplication.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.BatchProgramCourseTeacherDeleteDTO;
import com.cozentus.trainingtrackingapplication.dto.CourseWithTeachersDTO;
import com.cozentus.trainingtrackingapplication.dto.ProgramDTO;
import com.cozentus.trainingtrackingapplication.dto.StudentDTO;
import com.cozentus.trainingtrackingapplication.dto.TeacherDTO;
import com.cozentus.trainingtrackingapplication.model.Batch;
import com.cozentus.trainingtrackingapplication.model.Course;
import com.cozentus.trainingtrackingapplication.model.Program;
import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.model.Teacher;
import com.cozentus.trainingtrackingapplication.repository.BatchRepository;
import com.cozentus.trainingtrackingapplication.repository.CourseRepository;
import com.cozentus.trainingtrackingapplication.repository.ProgramRepository;
import com.cozentus.trainingtrackingapplication.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class BatchService {

	private BatchRepository batchRepository;

	private StudentRepository studentRepository;

	private ProgramRepository programRepository;

	private CourseRepository courseRepository;

	public BatchService(BatchRepository batchRepository, StudentRepository studentRepository,
			ProgramRepository programRepository, CourseRepository courseRepository) {
		this.batchRepository = batchRepository;
		this.studentRepository = studentRepository;
		this.programRepository = programRepository;
		this.courseRepository = courseRepository;

	}

	public Batch addBatch(Batch batch) {
		return batchRepository.save(batch);
	}

	public List<Batch> getAllBatches() {
		return batchRepository.findAll();
	}

	public Batch editBatch(Integer batchId, Batch batch) {
		Batch updatedBatch = batchRepository.findById(batchId)
				.orElseThrow(() -> new EntityNotFoundException("Batch not found with id: " + batch.getBatchId()));
		updatedBatch.setBatchName(batch.getBatchName());
		updatedBatch.setBatchCode(batch.getBatchCode());
		updatedBatch.setBatchStartDate(batch.getBatchStartDate());

		return batchRepository.save(updatedBatch);
	}

	@Transactional
	public Boolean deleteBatch(Integer batchId) {
		Batch batch = batchRepository.findById(batchId)
				.orElseThrow(() -> new EntityNotFoundException("Batch not found with id: " + batchId));

		for (Student student : batch.getStudents()) {
			student.getPrograms().clear();
			student.setBatch(null);
			studentRepository.save(student);
		}

		batchRepository.delete(batch);
		return true;
	}

	public List<ProgramDTO> getProgramsWithStudentsByBatchId(Integer batchId) {
		Batch batch = batchRepository.findByIdWithProgramsAndStudents(batchId)
				.orElseThrow(() -> new EntityNotFoundException("Batch not found with id: " + batchId));

		return convertToProgramDTOList(batch.getPrograms());
	}

	@Transactional
	public List<CourseWithTeachersDTO> getCoursesAndTeachersByBatchAndProgram(Integer batchId, Integer programId) {
		List<Object[]> courseData = batchRepository.findCoursesByBatchIdAndProgramId(batchId, programId);
		if (courseData.isEmpty()) {
			throw new EntityNotFoundException(
					"No courses found for batch id: " + batchId + " and program id: " + programId);
		}

		List<Course> courses = courseData.stream().map(this::mapToCourse).toList();

		List<Integer> courseIds = courses.stream().map(Course::getCourseId).toList();
		Map<Integer, Set<Teacher>> teachersByCourseId = fetchTeachersForCourses(courseIds);

		return convertToCourseDTOList(courses, teachersByCourseId);
	}

	private Course mapToCourse(Object[] data) {
		Course course = new Course();
		course.setCourseId((Integer) data[0]);
		course.setCourseName((String) data[1]);
		course.setCode((String) data[2]);
		course.setDescription((String) data[3]);
		course.setTheoryTime((Integer) data[4]);
		course.setPracticeTime((Integer) data[5]);
		return course;
	}

	private Map<Integer, Set<Teacher>> fetchTeachersForCourses(List<Integer> courseIds) {
		List<Object[]> results = courseRepository.findTeachersByCourseIds(courseIds);
		Map<Integer, Set<Teacher>> teachersByCourseId = new HashMap<>();
		for (Object[] result : results) {
			Integer courseId = ((Number) result[0]).intValue();
			Teacher teacher = new Teacher();
			teacher.setTeacherId(((Number) result[1]).intValue());
			teacher.setTeacherName((String) result[2]);
			teacher.setTeacherEmail((String) result[3]);
			teachersByCourseId.computeIfAbsent(courseId, k -> new HashSet<>()).add(teacher);
		}
		return teachersByCourseId;
	}

	private List<CourseWithTeachersDTO> convertToCourseDTOList(List<Course> courses,
			Map<Integer, Set<Teacher>> teachersByCourseId) {
		return courses.stream()
				.map(course -> convertToCourseDTO(course,
						teachersByCourseId.getOrDefault(course.getCourseId(), new HashSet<>())))
				.collect(Collectors.toList());
	}

	private CourseWithTeachersDTO convertToCourseDTO(Course course, Set<Teacher> teachers) {
		CourseWithTeachersDTO dto = new CourseWithTeachersDTO();
		dto.setCourseId(course.getCourseId());
		dto.setCourseName(course.getCourseName());
		dto.setCourseCode(course.getCode());
		dto.setTeachers(convertToTeacherDTOList(teachers));
		return dto;
	}

	public Boolean deleteBatchProgramCourseTeacherByBatchId(Integer batchId) {
		batchRepository.deleteByBatchId(batchId);
		return true;
	}

	@Transactional
	public void deleteProgramAndStudentsForBatch(Integer programId, Integer batchId) {
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new EntityNotFoundException("Program not found"));

		Batch batch = batchRepository.findById(batchId)
				.orElseThrow(() -> new EntityNotFoundException("Batch not found"));

		// Remove program from batch
		batch.getPrograms().remove(program);
		batchRepository.save(batch);

		// Find students associated with this program and batch
		Set<Student> studentsToUpdate = batch.getStudents().stream()
				.filter(student -> student.getPrograms().contains(program)).collect(Collectors.toSet());

		// Update students: remove the program, and if it's their only program, remove
		// from batch
		for (Student student : studentsToUpdate) {
			student.getPrograms().remove(program);

			// If student has no more programs in this batch, remove from batch
			if (student.getPrograms().stream().noneMatch(p -> batch.getPrograms().contains(p))) {
				student.setBatch(null);
			}

			studentRepository.save(student);
		}

		// Remove batch from program
		program.getBatches().remove(batch);
		programRepository.save(program);

		// Delete associated entries from batch_program_course_teacher table
		batchRepository.deleteByBatchAndProgramId(batchId, programId);
	}

	@Transactional
	public Boolean deleteByBatchIdAndProgramIdAndCourseIdAndTeacherId(BatchProgramCourseTeacherDeleteDTO dto) {
		Integer batchId = dto.getBatchId();
		Integer programId = dto.getProgramId();
		Integer courseId = dto.getCourseId();
		Integer teacherId = dto.getTeacherId();

		batchRepository.deleteByBatchIdAndProgramIdAndCourseIdAndTeacherId(batchId, programId, courseId, teacherId);
		return true;
	}

//	DTO TRANSFER LOGIC BEGINS HERE
	private List<ProgramDTO> convertToProgramDTOList(Set<Program> programs) {
		return programs.stream().map(this::convertToProgramDTO).toList();
	}

	private ProgramDTO convertToProgramDTO(Program program) {
		ProgramDTO dto = new ProgramDTO();
		dto.setProgramId(program.getProgramId());
		dto.setProgramName(program.getProgramName());
		dto.setProgramCode(program.getProgramCode());

		dto.setStudents(program.getStudents().stream().map(this::convertToStudentDTO).toList());

		return dto;
	}

	private StudentDTO convertToStudentDTO(Student student) {
		StudentDTO dto = new StudentDTO();
		dto.setStudentId(student.getStudentId());
		dto.setStudentName(student.getStudentName());
		dto.setStudentEmail(student.getStudentEmail());
		dto.setStudentCode(student.getStudentCode());
		return dto;
	}

	private List<TeacherDTO> convertToTeacherDTOList(Set<Teacher> teachers) {
		return teachers.stream().map(this::convertToTeacherDTO).toList();
	}

	private TeacherDTO convertToTeacherDTO(Teacher teacher) {
		TeacherDTO dto = new TeacherDTO();
		dto.setTeacherId(teacher.getTeacherId());
		dto.setTeacherName(teacher.getTeacherName());
		dto.setTeacherEmail(teacher.getTeacherEmail());
		return dto;
	}
}
