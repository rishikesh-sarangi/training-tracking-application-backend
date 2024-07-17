package com.cozentus.trainingtrackingapplication.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO {
    private Integer programId;
    private String programName;
    private String programCode;
    private List<StudentDTO> students;
}
