package com.mamlayev.studentapi.dto;

import com.mamlayev.studentapi.model.Student;

import java.time.Instant;

/**
 * Data returned to API clients.
 */
public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String groupName,
        Integer course,
        Instant createdAt,
        Instant updatedAt
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGroupName(),
                student.getCourse(),
                student.getCreatedAt(),
                student.getUpdatedAt());
    }
}
