package com.cozentus.trainingtrackingapplication.util;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseUtil {

    // Response status constants
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_ERROR = "ERROR";

    // Message type constants
    public static final String MESSAGE_TYPE_INFO = "INFO";
    public static final String MESSAGE_TYPE_WARNING = "WARNING";
    public static final String MESSAGE_TYPE_ERROR = "ERROR";

    /**
     * Processes the request based on the given type.
     * @param type The type of request to process
     * @return ResponseEntity containing the appropriate response
     */
    public static ResponseEntity<Object> processRequest(String type) {
        if (type == null) {
            return buildErrorResponse("Type parameter is required", HttpStatus.BAD_REQUEST);
        }

        switch (type.toLowerCase()) {
            case "success":
                return buildSuccessResponse(createSuccessData());
            case "custom":
                return buildCustomResponse(
                        STATUS_SUCCESS,
                        "This is a custom response",
                        createCustomData(),
                        HttpStatus.OK
                );
            default:
                return buildErrorResponse("Invalid type specified", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Creates data for a success response.
     * @return List containing success data
     */
    private static List<Map<String, String>> createSuccessData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data = new HashMap<>();
        data.put("message", "This is a success response");
        dataList.add(data);
        return dataList;
    }

    /**
     * Creates data for a custom response.
     * @return List containing custom data
     */
    private static List<Map<String, String>> createCustomData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data = new HashMap<>();
        data.put("customField", "Custom value");
        dataList.add(data);
        return dataList;
    }

    /**
     * Builds a success response.
     * @param data The data to include in the response
     * @return ResponseEntity with success status and data
     */
    public static <T> ResponseEntity<Object> buildSuccessResponse(List<T> data) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("statusCode", HttpStatus.OK.value());
        responseBody.put("statusMessage", STATUS_SUCCESS);
        responseBody.put("data", data);

        List<Map<String, Object>> response = new ArrayList<>();
        response.add(responseBody);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /**
     * Builds an error response.
     * @param message The error message
     * @param status The HTTP status code
     * @return ResponseEntity with error status and message
     */
    public static ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("statusCode", status.value());
        responseBody.put("statusMessage", message);
        responseBody.put("data", new ArrayList<>());

        List<Map<String, Object>> response = new ArrayList<>();
        response.add(responseBody);

        return new ResponseEntity<>(response, status);
    }

    /**
     * Builds a custom response.
     * @param status The response status
     * @param message The response message
     * @param data The data to include in the response
     * @param httpStatus The HTTP status code
     * @return ResponseEntity with custom fields and HTTP status
     */
    public static <T> ResponseEntity<Object> buildCustomResponse(String status, String message, List<T> data, HttpStatus httpStatus) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("statusCode", httpStatus.value());
        responseBody.put("statusMessage", message);
        responseBody.put("data", data);

        List<Map<String, Object>> response = new ArrayList<>();
        response.add(responseBody);

        return new ResponseEntity<>(response, httpStatus);
    }
}