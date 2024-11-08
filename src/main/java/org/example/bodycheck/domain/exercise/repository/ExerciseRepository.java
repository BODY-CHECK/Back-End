package org.example.bodycheck.domain.exercise.repository;

import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);
}
