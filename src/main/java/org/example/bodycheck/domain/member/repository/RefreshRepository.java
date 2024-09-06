package org.example.bodycheck.domain.member.repository;

import org.example.bodycheck.domain.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByMember_Id(Long memberId);
}
