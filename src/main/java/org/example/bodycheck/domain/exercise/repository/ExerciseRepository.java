package org.example.bodycheck.domain.exercise.repository;

import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findById(Long id);
    Optional<Exercise> findByName(String name);
    List<Exercise> findByType(ExerciseType type);
}
