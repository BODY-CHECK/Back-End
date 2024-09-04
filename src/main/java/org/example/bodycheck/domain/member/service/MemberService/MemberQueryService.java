package org.example.bodycheck.domain.member.service.MemberService;

import org.example.bodycheck.domain.member.entity.Member;

import java.util.Optional;

public interface MemberQueryService {

    Optional<Member> findMember(Long id);
    Member getMember();
}
