package org.example.bodycheck.service.MemberService;

import org.example.bodycheck.domain.Member;

public interface MemberCommandService {

    public Member getMemberByNickname(String nickname);
}
