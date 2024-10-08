package org.example.bodycheck.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.validation.annotation.ExistSolution;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.service.SolutionQueryService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SolutionExistsValidator implements ConstraintValidator<ExistSolution, Long> {

    private final SolutionQueryService solutionQueryService;

    @Override
    public void initialize(ExistSolution constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Solution> target = solutionQueryService.findSolution(value);

        if(target.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.SOLUTION_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }

        return true;
    }
}
