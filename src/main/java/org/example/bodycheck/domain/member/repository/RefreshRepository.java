package org.example.bodycheck.domain.member.repository;

import java.util.Optional;

import org.example.bodycheck.domain.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

@Deprecated
public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {

	boolean existsByMember_Id(Long memberId);

	boolean existsByRefreshToken(String refreshToken);

	Optional<RefreshToken> findByRefreshToken(String refreshToken);

	Optional<RefreshToken> findByMember_Id(Long memberId);
}
