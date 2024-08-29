package org.example.bodycheck.repository;

import org.example.bodycheck.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByNickname(String nickname);
    boolean existsByNickname(String nickname);
}
