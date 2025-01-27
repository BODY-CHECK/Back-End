package org.example.bodycheck.domain.member.repository;

import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Member> findById(Long memberId);
}
