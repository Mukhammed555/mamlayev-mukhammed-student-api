package com.mamlayev.studentapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for creating or updating a student.
 */
public record StudentRequest(
        @NotBlank(message = "firstName is required")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "lastName is required")
        @Size(max = 100)
        String lastName,

        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid e-mail address")
        @Size(max = 150)
        String email,

        @Size(max = 50)
        String groupName,

        @Min(value = 1, message = "course must be between 1 and 6")
        @Max(value = 6, message = "course must be between 1 and 6")
        Integer course
) {
}
