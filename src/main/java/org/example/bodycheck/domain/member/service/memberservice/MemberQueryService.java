package org.example.bodycheck.domain.member.service.memberservice;

import java.time.LocalDate;
import java.util.Optional;

import org.example.bodycheck.domain.enums.LoginType;
import org.example.bodycheck.domain.member.entity.Member;

public interface MemberQueryService {

	Optional<Member> findMember(Long id);

	Member getMember();

	boolean isRegisteredWithEmail(Member member);

	boolean isRegisteredWithSocial(Member member, LoginType loginType);

	boolean isPremium(LocalDate premiumExpiredAt);
}
