package org.example.bodycheck.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.validation.annotation.ExistExercise;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.exercise.service.ExerciseQueryService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExerciseExistsValidator implements ConstraintValidator<ExistExercise, Long> {

    private final ExerciseQueryService exerciseQueryService;

    @Override
    public void initialize(ExistExercise constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Exercise> target = exerciseQueryService.findExercise(value);

        if(target.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.EXERCISE_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }

        return true;
    }
}
