package org.example.bodycheck.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.attendance.entity.Attendance;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.enums.Gender;
import org.example.bodycheck.domain.enums.LoginType;
import org.example.bodycheck.external.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.domain.routine.entity.Routine;
import org.example.bodycheck.domain.solution.entity.Solution;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false)
    private String email;

    private String pw;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Float height;

    private Float weight;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private LocalDate inactiveDate;

//    // 이전 로직 - 리프레시 토큰을 DB에 저장 할 경우
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<RefreshToken> refreshToken = new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<FcmToken> fcmTokenList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Attendance> attendanceList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Routine> routineList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Solution> solutionList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private KakaoPay kakaoPay;

    public void updatePw(String pw) { this.pw = pw; }

    public void updateProfile(String nickname, ExerciseType exerciseType) {
        this.nickname = nickname;
        this.exerciseType = exerciseType;
    }

    public void deactivate(LocalDate inactiveDate) { this.inactiveDate = inactiveDate; }

    public void initRoutineList(List<Routine> routineList) { this.routineList = routineList; }
}
