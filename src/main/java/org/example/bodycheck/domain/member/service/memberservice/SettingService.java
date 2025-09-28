package org.example.bodycheck.domain.member.service.memberservice;

import org.example.bodycheck.common.apipayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingService {

	private final MemberRepository memberRepository;

	public MemberResponseDto.MemberSettingDto profileSetting(Member member,
		MemberRequestDto.MemberProfileSettingDto profileSettingDto) {
		if (memberRepository.existsByNickname(profileSettingDto.getNickname())) {
			if (!member.getNickname().equals(profileSettingDto.getNickname())) {
				throw new GeneralHandler(ErrorStatus.NICKNAME_ALREADY_EXISTS);
			}
		}

		member.updateProfile(profileSettingDto.getNickname(), profileSettingDto.getExerciseType());

		memberRepository.save(member);

		return MemberResponseDto.MemberSettingDto.builder()
			.memberId(member.getId())
			.build();
	}
}
