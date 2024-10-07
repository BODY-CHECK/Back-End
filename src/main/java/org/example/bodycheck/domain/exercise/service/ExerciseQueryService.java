package org.example.bodycheck.domain.exercise.service;

import org.example.bodycheck.domain.exercise.entity.Exercise;

import java.util.Optional;

public interface ExerciseQueryService {

    Optional<Exercise> findExercise(Long id);
}
