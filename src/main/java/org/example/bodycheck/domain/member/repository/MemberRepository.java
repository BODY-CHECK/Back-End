package org.example.bodycheck.domain.member.repository;

import java.util.Optional;

import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByNickname(String nickname);

	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);
}
