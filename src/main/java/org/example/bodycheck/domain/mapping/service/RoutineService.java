package org.example.bodycheck.domain.mapping.service;

import org.example.bodycheck.domain.mapping.dto.RoutineDTO;
import org.example.bodycheck.domain.member.entity.Member;

public interface RoutineService {
    RoutineDTO deleteRoutine(Long routineId);

    RoutineDTO createRoutine(RoutineDTO routineDTO);
}
