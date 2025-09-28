package org.example.bodycheck.domain.exercise.service;

import java.util.Optional;

import org.example.bodycheck.domain.exercise.entity.Exercise;

public interface ExerciseQueryService {

	Optional<Exercise> findExercise(Long id);
}
