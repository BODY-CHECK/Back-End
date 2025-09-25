package org.example.bodycheck.domain.appversion.dto;

import lombok.Getter;

public class AppVersionRequestDto {

	@Getter
	public static class AppVersionDto {
		private String latestVersion;
		private String minSupportedVersion;
		private String releaseNotes;
	}
}
