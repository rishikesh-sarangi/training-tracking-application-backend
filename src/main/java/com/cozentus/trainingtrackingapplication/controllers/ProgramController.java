package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;
import java.util.Optional;
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
	public ResponseEntity<Program> createProgram(@RequestBody Program program) {
		Optional<Program> createdProgram = Optional.ofNullable(programService.addProgram(program));
		if (createdProgram.isPresent()) {
			return new ResponseEntity<>(createdProgram.get(), HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{programId}")
	public ResponseEntity<Program> updateProgram(@PathVariable Integer programId, @RequestBody Program program) {
		Optional<Program> updatedProgram = Optional.ofNullable(programService.editProgram(programId, program));
		if (updatedProgram.isPresent()) {
			return new ResponseEntity<>(updatedProgram.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
