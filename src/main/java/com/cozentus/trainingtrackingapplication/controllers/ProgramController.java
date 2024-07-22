package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.model.Program;
import com.cozentus.trainingtrackingapplication.service.ProgramService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/programs")
public class ProgramController {

	private ProgramService programService;

	ProgramController(ProgramService programService) {

		this.programService = programService;
	}

	@GetMapping
	public ResponseEntity<List<Program>> getAllPrograms() {
		Optional<List<Program>> programs = Optional.ofNullable(programService.getAllPrograms());

		if (programs.isPresent()) {
			return new ResponseEntity<>(programs.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<Object> createProgram(@RequestBody Program program) {
		try {
			Program createdProgram = programService.addProgram(program);
			return new ResponseEntity<>(createdProgram, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{programId}")
	public ResponseEntity<Object> updateProgram(@PathVariable Integer programId, @RequestBody Program program) {
		try {
			Program updatedProgram = programService.editProgram(programId, program);
			return new ResponseEntity<>(updatedProgram, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		catch(EntityNotFoundException e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{programId}")
	public ResponseEntity<Boolean> deleteProgram(@PathVariable Integer programId) {
		Optional<Boolean> deletedProgram = Optional.of(programService.deleteProgram(programId));
		if (deletedProgram.isPresent()) {
			return new ResponseEntity<>(deletedProgram.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
