package com.mspp.restaurantservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponse {

    @Schema(
            description = "HTTP status code",
            example = "400"
    )
    private int status;

    @Schema(
            description = "Error message",
            example = "Validation failed"
    )
    private String message;

    @Schema(
            description = "Time when the error occurred",
            example = "2026-10-14T21:30:00"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Validation errors where the key is the invalid field",
            example = """
                {
                  "email": "must be a well-formed email address",
                  "name": "must not be blank"
                }
                """
    )
    private Map<String, String> errors;
}