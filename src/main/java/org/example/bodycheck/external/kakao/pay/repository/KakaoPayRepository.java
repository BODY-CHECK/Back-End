package org.example.bodycheck.external.kakao.pay.repository;

import org.example.bodycheck.external.kakao.pay.entity.KakaoPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KakaoPayRepository extends JpaRepository<KakaoPay, Long> {

    boolean existsByMember_Id(Long memberId);
    Optional<KakaoPay> findByMember_Id(Long memberId);
    Optional<KakaoPay> findByTid(String tid);
    @Query("SELECT k FROM KakaoPay k JOIN FETCH k.member WHERE k.sid IS NOT NULL")
    List<KakaoPay> findAllWithMemberAndSidNotNull();
}
