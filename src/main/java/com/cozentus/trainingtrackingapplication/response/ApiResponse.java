package com.cozentus.trainingtrackingapplication.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	
	private Integer responseCode;
	private String message;
	private T data;
}
