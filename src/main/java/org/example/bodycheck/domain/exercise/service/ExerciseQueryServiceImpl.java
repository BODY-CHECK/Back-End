package org.example.bodycheck.domain.exercise.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.exercise.repository.ExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseQueryServiceImpl implements ExerciseQueryService {

    private final ExerciseRepository exerciseRepository;

    @Override
    public Optional<Exercise> findExercise(Long id) {
        return exerciseRepository.findById(id);
    }
}
