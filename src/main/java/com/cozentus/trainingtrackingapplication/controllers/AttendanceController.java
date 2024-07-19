package com.cozentus.trainingtrackingapplication.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.dto.AttendanceDTO;
import com.cozentus.trainingtrackingapplication.dto.AttendanceEditDTO;
import com.cozentus.trainingtrackingapplication.model.Attendance;
import com.cozentus.trainingtrackingapplication.service.AttendanceService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	@PostMapping
	public ResponseEntity<Object> createEvaluation(@RequestBody Attendance attendance) {
		try {
			Attendance createdAttendance = attendanceService.addAttendance(attendance);
			return ResponseUtil.buildSuccessResponse(Collections.singletonList(createdAttendance));
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error creating attendance entry: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity<Object> getAllAttendances() {
		try {
			return ResponseUtil.buildSuccessResponse(attendanceService.getAllAttendances());
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error fetching attendances: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	this is a get request i'm using post mapping for filtering
	@PostMapping("/filter")
	public ResponseEntity<Object> getAttendancesByFilters(@RequestBody AttendanceDTO filterDTO) {
		try {
			List<Attendance> attendances = attendanceService.getAttendanceByTeacherBatchProgramAndCourse(
					filterDTO.getTeacherId(), filterDTO.getBatchId(), filterDTO.getProgramId(),
					filterDTO.getCourseId(), filterDTO.getAttendanceDate());
			return ResponseUtil.buildSuccessResponse(attendances);
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error fetching attendances: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{attendanceId}")
	public ResponseEntity<Object> deleteAllAttendances(@PathVariable Integer attendanceId) {
		try {
			attendanceService.deleteAttendance(attendanceId);
			return ResponseUtil.buildSuccessResponse(Collections.emptyList());
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error deleting attendances: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping("/{attendanceId}")
	public ResponseEntity<Object> updateAttendance(@PathVariable Integer attendanceId,
			@RequestBody AttendanceEditDTO attendanceEditDTO) {
		try {
			Attendance updatedAttendance = attendanceService.editAttendance(attendanceId, attendanceEditDTO);
			return ResponseUtil.buildSuccessResponse(Collections.singletonList(updatedAttendance));
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error updating attendance: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
//	this is a get request i'm using post mapping for filtering
	@PostMapping("/by-batch-program-teacher")
	public ResponseEntity<Object> getAttendanceByTeacherBatchProgramAndCourse(@RequestBody AttendanceDTO filterDTO) {
		try {
			List<Attendance> attendances = attendanceService.findByBatchIdAndProgramIdAndTeacherId(filterDTO);
			return ResponseUtil.buildSuccessResponse(attendances);
		} catch (Exception e) {
			return ResponseUtil.buildErrorResponse("Error fetching attendances: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
