package org.example.bodycheck.domain.appversion.entity;

import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.enums.DeviceOsType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AppVersion extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private DeviceOsType deviceOS;

	private String latestVersion;

	private String minSupportedVersion;

	private String updateUrl;

	private String releaseNotes;

	public void updateAppVersion(String latestVersion, String minSupportedVersion, String releaseNotes) {
		this.latestVersion = latestVersion;
		this.minSupportedVersion = minSupportedVersion;
		this.releaseNotes = releaseNotes;
	}
}
