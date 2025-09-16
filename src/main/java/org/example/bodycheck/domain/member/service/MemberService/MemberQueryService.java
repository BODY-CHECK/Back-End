package org.example.bodycheck.domain.member.service.MemberService;

import org.example.bodycheck.domain.enums.LoginType;
import org.example.bodycheck.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberQueryService {

    Optional<Member> findMember(Long id);
    Member getMember();
    // List<FcmToken> findFirebaseTokenList(Long memberId);
    boolean isRegisteredWithEmail(Member member);
    boolean isRegisteredWithSocial(Member member, LoginType loginType);
}
