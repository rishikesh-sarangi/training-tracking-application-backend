package com.cozentus.trainingtrackingapplication.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cozentus.trainingtrackingapplication.response.ApiResponse;

public class ResponseUtil {

    /**
     * Builds a success response with HTTP status OK.
     * @param data The data to include in the response body
     * @param <T> The type of the data
     * @return ResponseEntity containing success response with data
     */
    public static <T> ResponseEntity<Object> buildSuccessResponse(List<T> data) {
        ApiResponse<List<T>> response = new ApiResponse<>(HttpStatus.OK.value(), "Success", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Builds an error response with a specific message and HTTP status.
     * @param message The error message
     * @param httpStatus The HTTP status code
     * @param <T> The type of data in the response (typically Void for error responses)
     * @return ResponseEntity containing error response with message
     */
    public static <T> ResponseEntity<Object> buildErrorResponse(String message, HttpStatus httpStatus) {
        ApiResponse<T> response = new ApiResponse<>(httpStatus.value(), message, null);
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Generates a custom response with specified HTTP status, data, and message.
     * @param httpStatus The HTTP status code
     * @param data The data to include in the response body
     * @param message The message associated with the response
     * @param <T> The type of the data
     * @return ResponseEntity containing custom response with data and message
     */
    public static <T> ResponseEntity<Object> generateResponse(HttpStatus httpStatus, List<T> data, String message) {
        ApiResponse<List<T>> response = new ApiResponse<>(httpStatus.value(), message, data);
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Builds a generic error response with HTTP status INTERNAL_SERVER_ERROR.
     * @return ResponseEntity containing generic error response
     */
    public static ResponseEntity<Object> buildGenericErrorResponse() {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong. Please try again later.", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
