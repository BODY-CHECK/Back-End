package org.example.bodycheck.domain.member.service.memberservice;

import org.example.bodycheck.domain.enums.LoginType;
import org.example.bodycheck.domain.member.entity.Member;

import java.util.Optional;

public interface MemberQueryService {

    Optional<Member> findMember(Long id);
    Member getMember();
    boolean isRegisteredWithEmail(Member member);
    boolean isRegisteredWithSocial(Member member, LoginType loginType);
}
