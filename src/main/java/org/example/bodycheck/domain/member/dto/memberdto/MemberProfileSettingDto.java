package org.example.bodycheck.domain.member.dto.memberdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.bodycheck.domain.enums.ExerciseType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileSettingDto {

    @NotBlank
    @Size(max = 10, message = "이름은 10자 이내여야 합니다.")
    String nickname;
    ExerciseType exerciseType;
}
