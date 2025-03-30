package org.example.bodycheck.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;

@Deprecated
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken extends BaseEntity { // 이전 로직 - 리프레시 토큰을 DB에 저장 할 경우

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY) // 원래는 OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
