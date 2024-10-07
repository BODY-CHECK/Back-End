package org.example.bodycheck.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY) // 원래는 OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
