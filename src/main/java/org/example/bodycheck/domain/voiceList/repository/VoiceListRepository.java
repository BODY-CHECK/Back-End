package org.example.bodycheck.domain.voiceList.repository;

import org.example.bodycheck.domain.enums.VoiceCode;
import org.example.bodycheck.domain.voiceList.entity.VoiceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoiceListRepository extends JpaRepository<VoiceList, Long> {

    Optional<VoiceList> findByVoiceCode(VoiceCode voiceCode);
}
