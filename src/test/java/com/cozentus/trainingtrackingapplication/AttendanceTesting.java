package com.cozentus.trainingtrackingapplication;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cozentus.trainingtrackingapplication.controllers.AttendanceController;
import com.cozentus.trainingtrackingapplication.dto.AttendanceEditDTO;
import com.cozentus.trainingtrackingapplication.model.Attendance;
import com.cozentus.trainingtrackingapplication.service.AttendanceService;
import com.cozentus.trainingtrackingapplication.repository.AttendanceRepository;
import com.cozentus.trainingtrackingapplication.repository.TopicRepository;

public class AttendanceTesting {

    @InjectMocks
    private AttendanceService attendanceService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private AttendanceController attendanceController;

    @Mock
    private AttendanceService mockAttendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // AttendanceService tests
    @Test
    void testGetAllAttendances1() {
        Attendance attendance1 = new Attendance();
        Attendance attendance2 = new Attendance();
        List<Attendance> attendances = Arrays.asList(attendance1, attendance2);

        when(attendanceRepository.findAll()).thenReturn(attendances);

        List<Attendance> result = attendanceService.getAllAttendances();

        assertEquals(2, result.size());
        verify(attendanceRepository, times(1)).findAll();
    }

    @Test
    void testAddAttendance() {
        Attendance attendance = new Attendance();
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        Attendance result = attendanceService.addAttendance(attendance);

        assertNotNull(result);
        verify(attendanceRepository, times(1)).save(attendance);
    }

    @Test
    void testDeleteAttendance() {
        Attendance attendance = new Attendance();
        when(attendanceRepository.findById(anyInt())).thenReturn(Optional.of(attendance));

        boolean result = attendanceService.deleteAttendance(1);

        assertTrue(result);
        verify(attendanceRepository, times(1)).deleteById(1);
    }

    

    @Test
    void testGetAttendanceByTeacherBatchProgramAndCourse1() {
        Attendance attendance = new Attendance();
        List<Attendance> attendances = Arrays.asList(attendance);
        LocalDate date = LocalDate.now();

        when(attendanceRepository.findAttendance(anyInt(), anyInt(), anyInt(), anyInt(), any(LocalDate.class)))
                .thenReturn(attendances);

        List<Attendance> result = attendanceService.getAttendanceByTeacherBatchProgramAndCourse(1, 1, 1, 1, date);

        assertEquals(1, result.size());
        verify(attendanceRepository, times(1)).findAttendance(1, 1, 1, 1, date);
    }

     
    // AttendanceController tests
  

    @Test
    void testGetAllAttendances() {
        Attendance attendance = new Attendance();
        List<Attendance> attendances = Collections.singletonList(attendance);
        when(mockAttendanceService.getAllAttendances()).thenReturn(attendances);

        ResponseEntity<Object> response = attendanceController.getAllAttendances();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mockAttendanceService, times(1)).getAllAttendances();
    }

    

    @Test
    void testDeleteAllAttendances() {
        when(mockAttendanceService.deleteAttendance(anyInt())).thenReturn(true);

        ResponseEntity<Object> response = attendanceController.deleteAllAttendances(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mockAttendanceService, times(1)).deleteAttendance(1);
    }

    @Test
    void testUpdateAttendance() {
        Attendance attendance = new Attendance();
        AttendanceEditDTO editDTO = new AttendanceEditDTO();
        when(mockAttendanceService.editAttendance(anyInt(), any(AttendanceEditDTO.class))).thenReturn(attendance);

        ResponseEntity<Object> response = attendanceController.updateAttendance(1, editDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mockAttendanceService, times(1)).editAttendance(anyInt(), any(AttendanceEditDTO.class));
    }

     
}