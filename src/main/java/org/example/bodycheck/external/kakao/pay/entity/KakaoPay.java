package org.example.bodycheck.external.kakao.pay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.member.entity.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoPay extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tid;

    private String sid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateTid(String tid) { this.tid = tid; }

    public void updateSid(String sid) { this.sid = sid; }

    public void updatePayInfo(String tid, String sid) {
        this.tid = tid;
        this.sid = sid;
    }
}
