package org.example.bodycheck.domain.tts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.enums.NoiseType;
import org.example.bodycheck.domain.exercise.entity.Exercise;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Tts extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer ttsIdx; // 1 ~ 32 (평가 지표 6개)

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "hits")
    private Long hits;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
}
