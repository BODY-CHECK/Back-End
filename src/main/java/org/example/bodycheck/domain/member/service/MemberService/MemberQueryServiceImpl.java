package org.example.bodycheck.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.jwt.JwtTokenProvider;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Optional<Member> findMember(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMember() {
        // 토큰이 유효한지 검증
        /*if (!jwtTokenProvider.validateToken(token)) {
            throw new TempHandler(ErrorStatus.TOKEN_NOT_EXIST);
        }

         */

        // 토큰에서 인증 정보를 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication + " authentication");

        if (authentication == null) {
            throw new GeneralHandler(ErrorStatus.TOKEN_MISSING_AUTHORITY);
        }

        // 인증 정보에서 사용자 이메일을 가져와 회원 조회
        String email = authentication.getName();

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
