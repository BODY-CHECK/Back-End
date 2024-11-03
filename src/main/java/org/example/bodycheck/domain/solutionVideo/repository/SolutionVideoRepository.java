package org.example.bodycheck.domain.solutionVideo.repository;

import org.example.bodycheck.domain.solutionVideo.entity.SolutionVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolutionVideoRepository extends JpaRepository<SolutionVideo, Long> {

    boolean existsBySolution_Id(Long solutionId);
    Optional<SolutionVideo> findBySolution_Id(Long solutionId);
}
