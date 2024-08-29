package org.example.bodycheck.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.apiPayload.exception.handler.TempHandler;
import org.example.bodycheck.domain.Member;
import org.example.bodycheck.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberByNickname(String nickname) {

        if (!memberRepository.existsByNickname(nickname)) {
            throw new TempHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return memberRepository.findByNickname(nickname);
    }
}
