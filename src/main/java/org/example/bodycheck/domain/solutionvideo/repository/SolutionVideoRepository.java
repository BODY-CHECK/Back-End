package org.example.bodycheck.domain.solutionvideo.repository;

import java.util.Optional;

import org.example.bodycheck.domain.solutionvideo.entity.SolutionVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionVideoRepository extends JpaRepository<SolutionVideo, Long> {

	boolean existsBySolution_Id(Long solutionId);

	Optional<SolutionVideo> findBySolution_Id(Long solutionId);
}
