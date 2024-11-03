package org.example.bodycheck.domain.tts.repository;

import org.example.bodycheck.domain.tts.entity.Tts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TtsRepository extends JpaRepository<Tts, Long> {
    List<Tts> findByExerciseId(Long exerciseId);
    Optional<Tts> findByExercise_IdAndTtsIdx(Long exerciseId, Integer ttsIdx);
}
