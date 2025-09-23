package org.example.bodycheck.domain.exercise.repository;

import java.util.List;

import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

	List<Exercise> findByType(ExerciseType type);
}
