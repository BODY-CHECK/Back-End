package org.example.bodycheck.domain.kakao_pay.repository;

import org.example.bodycheck.domain.kakao_pay.entity.KakaoPay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoPayRepository extends JpaRepository<KakaoPay, Long> {

    boolean existsByMember_Id(Long memberId);
    Optional<KakaoPay> findByMember_Id(Long memberId);
    Optional<KakaoPay> findByTid(String tid);
}
