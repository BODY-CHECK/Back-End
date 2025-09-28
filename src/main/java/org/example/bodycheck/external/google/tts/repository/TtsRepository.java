package org.example.bodycheck.external.google.tts.repository;

import java.util.Optional;

import org.example.bodycheck.external.google.tts.entity.Tts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TtsRepository extends JpaRepository<Tts, Long> {

	Optional<Tts> findByExercise_IdAndTtsIdx(Long exerciseId, Integer ttsIdx);
}
