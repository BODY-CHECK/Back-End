package org.example.bodycheck.domain.exercise.repository;

import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
