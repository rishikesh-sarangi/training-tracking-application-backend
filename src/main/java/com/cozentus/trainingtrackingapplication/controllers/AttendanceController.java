package com.cozentus.trainingtrackingapplication.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.dto.AttendanceFilterDTO;
import com.cozentus.trainingtrackingapplication.dto.AttendanceDTO;
import com.cozentus.trainingtrackingapplication.dto.AttendanceEditDTO;
import com.cozentus.trainingtrackingapplication.model.Attendance;
import com.cozentus.trainingtrackingapplication.service.AttendanceService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/attendance")
public class AttendanceController {

	private AttendanceService attendanceService;

	AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostMapping
	public ResponseEntity<Object> createEvaluation(@RequestBody AttendanceDTO attendanceDto) {
		try {
			Attendance attendance = attendanceService.convertToEntity(attendanceDto);
			Attendance createdAttendance = attendanceService.addAttendance(attendance);
			if (createdAttendance != null) {
				return ResponseUtil.buildSuccessResponse(Collections.singletonList(createdAttendance));
			} else {
				return ResponseUtil.buildErrorResponse("Error creating attendance entry", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

	@GetMapping
	public ResponseEntity<Object> getAllAttendances() {
		try {
			List<Attendance> attendances = attendanceService.getAllAttendances();
			if (!attendances.isEmpty()) {
				return ResponseUtil.buildSuccessResponse(attendances);
			} else {
				return ResponseUtil.buildErrorResponse("No attendances found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

//	this is a get request i'm using post mapping for filtering
	@PostMapping("/filter")
	public ResponseEntity<Object> getAttendancesByFilters(@RequestBody AttendanceFilterDTO filterDTO) {
		try {
			List<Attendance> attendances = attendanceService.getAttendanceByTeacherBatchProgramAndCourse(
					filterDTO.getTeacherId(), filterDTO.getBatchId(), filterDTO.getProgramId(), filterDTO.getCourseId(),
					filterDTO.getAttendanceDate());
			if (!attendances.isEmpty()) {
				return ResponseUtil.buildSuccessResponse(attendances);
			} else {
				return ResponseUtil.buildErrorResponse("No attendances found!", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

	@DeleteMapping("/{attendanceId}")
	public ResponseEntity<Object> deleteAllAttendances(@PathVariable Integer attendanceId) {
		try {
			Boolean response = attendanceService.deleteAttendance(attendanceId);
			if (response.equals(true)) {
				return ResponseUtil.buildSuccessResponse(Collections.emptyList());
			} else {
				return ResponseUtil.buildErrorResponse("Attendance not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

	@PatchMapping("/{attendanceId}")
	public ResponseEntity<Object> updateAttendance(@PathVariable Integer attendanceId,
			@RequestBody AttendanceEditDTO attendanceEditDTO) {
		try {
			Attendance updatedAttendance = attendanceService.editAttendance(attendanceId, attendanceEditDTO);
			if (updatedAttendance != null) {
				return ResponseUtil.buildSuccessResponse(Collections.singletonList(updatedAttendance));
			} else {
				return ResponseUtil.buildErrorResponse("Attendance not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}

//	this is a get request i'm using post mapping for filtering
	@PostMapping("/by-batch-program-teacher")
	public ResponseEntity<Object> getAttendanceByTeacherBatchProgramAndCourse(@RequestBody AttendanceFilterDTO filterDTO) {
		try {
			List<Attendance> attendances = attendanceService.findByBatchIdAndProgramIdAndTeacherId(filterDTO);
			if (!attendances.isEmpty()) {
				return ResponseUtil.buildSuccessResponse(attendances);
			} else {
				return ResponseUtil.buildErrorResponse("No Attendances found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseUtil.buildGenericErrorResponse();
		}
	}
}
