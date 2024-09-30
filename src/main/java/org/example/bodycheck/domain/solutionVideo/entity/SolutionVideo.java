package org.example.bodycheck.domain.solutionVideo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.mapping.Solution;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SolutionVideo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY) // 원래는 OneToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;
}
