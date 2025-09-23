package org.example.bodycheck.domain.member.entity;

import org.example.bodycheck.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
