package org.example.bodycheck.common.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.example.bodycheck.common.validation.validator.SolutionExistsValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = SolutionExistsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistSolution {
	String message() default "해당 솔루션이 존재하지 않습니다";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
