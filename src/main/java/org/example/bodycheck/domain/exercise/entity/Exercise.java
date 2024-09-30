package org.example.bodycheck.domain.exercise.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.mapping.Routine;
import org.example.bodycheck.domain.mapping.Solution;
import org.example.bodycheck.domain.tts.entity.Tts;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Exercise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private ExerciseType type;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<Routine> routineList = new ArrayList<>();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<Solution> solutionList = new ArrayList<>();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<Tts> ttsList = new ArrayList<>();
}
