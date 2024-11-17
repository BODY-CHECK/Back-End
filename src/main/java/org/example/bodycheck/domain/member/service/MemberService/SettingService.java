package org.example.bodycheck.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberProfileSettingDTO;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberSettingDTO;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final MemberRepository memberRepository;

    public MemberSettingDTO profileSetting(Member member, MemberProfileSettingDTO profileSettingDTO) {
        if(memberRepository.existsByNickname(profileSettingDTO.getNickname())) {
            if (!member.getNickname().equals(profileSettingDTO.getNickname())) {
                throw new GeneralHandler(ErrorStatus.NICKNAME_ALREADY_EXISTS);
            }
        }

            member.setNickname(profileSettingDTO.getNickname());

        member.setExerciseType(profileSettingDTO.getExerciseType());

        memberRepository.save(member);

        return MemberSettingDTO.builder()
                .memberId(member.getId())
                .build();
    }
}
