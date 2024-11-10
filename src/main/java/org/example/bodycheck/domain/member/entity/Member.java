package org.example.bodycheck.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.attendance.entity.Attendance;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.enums.Gender;
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

    private String tid;

    private String sid;

    private boolean premium = false;

    private LocalDate inactiveDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // 원래는 OneToOne
    private List<RefreshToken> refreshToken = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Attendance> attendanceList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Routine> routineList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Solution> solutionList = new ArrayList<>();

    public void setPw(String pw) { this.pw = pw; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public void setRoutineList(List<Routine> routineList) { this.routineList = routineList; }

    public void setTid(String tid) { this.tid = tid; }

    public void setSid(String sid) { this.sid = sid; }
}
