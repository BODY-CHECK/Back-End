package org.example.bodycheck.common.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.bodycheck.common.validation.validator.ExerciseExistsValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExerciseExistsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistExercise {
    String message() default "해당 운동이 존재하지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
