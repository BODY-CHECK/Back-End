package org.example.bodycheck.domain.member.dto.MemberDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileSettingDTO {

    @NotBlank
    @Size(max = 5, message = "이름은 5자 이내여야 합니다.")
    String nickname;
}
