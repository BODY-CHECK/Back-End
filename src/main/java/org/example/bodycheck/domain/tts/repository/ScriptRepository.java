package org.example.bodycheck.domain.tts.repository;

import org.example.bodycheck.domain.tts.entity.Tts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends JpaRepository<Tts, Long> {
}
