package com.mamlayev.studentapi.repository;

import com.mamlayev.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmailIgnoreCase(String email);

    /** Used by update: is the e-mail taken by a different student? */
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
