package com.mamlayev.studentapi.service;

import com.mamlayev.studentapi.dto.StudentRequest;
import com.mamlayev.studentapi.dto.StudentResponse;
import com.mamlayev.studentapi.exception.DuplicateEmailException;
import com.mamlayev.studentapi.exception.StudentNotFoundException;
import com.mamlayev.studentapi.model.Student;
import com.mamlayev.studentapi.repository.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for students.
 */
@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return repository.findAll(Sort.by("id")).stream()
                .map(StudentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return StudentResponse.from(getStudent(id));
    }

    public StudentResponse create(StudentRequest request) {
        if (repository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        Student student = new Student(
                request.firstName().trim(),
                request.lastName().trim(),
                request.email().trim().toLowerCase(),
                request.groupName(),
                request.course());
        return StudentResponse.from(repository.save(student));
    }

    /**
     * Update operation (added by Mukhammed Mamlayev on feature/mamlayev-mukhammed-crud).
     * Replaces all fields, returns 404 for an unknown id and 409 if the new
     * e-mail already belongs to another student.
     */
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = getStudent(id);
        if (repository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) {
            throw new DuplicateEmailException(request.email());
        }
        student.setFirstName(request.firstName().trim());
        student.setLastName(request.lastName().trim());
        student.setEmail(request.email().trim().toLowerCase());
        student.setGroupName(request.groupName());
        student.setCourse(request.course());
        return StudentResponse.from(repository.saveAndFlush(student));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private Student getStudent(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }
}
