package org.example.bodycheck.domain.member.dto.memberdto;

import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.enums.Gender;
import org.example.bodycheck.domain.enums.LoginType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class MemberRequestDto {

	@Getter
	public static class SignUpDto {
		private String nickname;
		private Float height;
		private Float weight;
		private Gender gender;
		@Email(message = "유효한 이메일 주소를 입력해주세요.")
		private String email;
		//@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@$%^&*])[a-zA-Z0-9!@$%^&*]{8,}",
		//message = "비밀번호는 영문, 숫자, 특수문자 '!,@,$,%,^,&,*' 를 포함해야 하며, 최소 8자 이상이어야 합니다.")
		private String pw;
		private ExerciseType exerciseType;
		private LoginType loginType;
	}

	@Getter
	public static class SignInDto {
		@Email(message = "유효한 이메일 주소를 입력해주세요.")
		private String email;
		private String pw;
		private LoginType loginType;
	}

	@Getter
	public static class SocialLoginDto {
		@Email(message = "유효한 이메일 주소를 입력해주세요.")
		private String email;
	}

	@Getter
	public static class PasswordDto {
		private String pw;
	}

	@Getter
	public static class AccessTokenDto {
		private String accessToken;
	}

	@Getter
	public static class RefreshTokenDto {
		private String refreshToken;
	}

	@Getter
	public static class MemberProfileSettingDto {

		@NotBlank
		@Size(max = 10, message = "이름은 10자 이내여야 합니다.")
		private String nickname;
		private ExerciseType exerciseType;
	}
}
