package org.example.bodycheck.service.MemberService;

import org.example.bodycheck.domain.Member;

import java.util.Optional;

public interface MemberQueryService {

    Optional<Member> findMember(Long id);
}
