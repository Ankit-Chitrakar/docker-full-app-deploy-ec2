package com.ankit.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private Instant timestamp;

    // Used for returning field specific validation errors
    private Map<String, String> validationErrors;
}
