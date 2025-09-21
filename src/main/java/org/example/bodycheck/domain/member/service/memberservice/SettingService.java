package org.example.bodycheck.domain.member.service.memberservice;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final MemberRepository memberRepository;

    public MemberResponseDto.MemberSettingDto profileSetting(Member member, MemberRequestDto.MemberProfileSettingDto profileSettingDTO) {
        if(memberRepository.existsByNickname(profileSettingDTO.getNickname())) {
            if (!member.getNickname().equals(profileSettingDTO.getNickname())) {
                throw new GeneralHandler(ErrorStatus.NICKNAME_ALREADY_EXISTS);
            }
        }

        member.updateProfile(profileSettingDTO.getNickname(), profileSettingDTO.getExerciseType());

        memberRepository.save(member);

        return MemberResponseDto.MemberSettingDto.builder()
                .memberId(member.getId())
                .build();
    }
}
