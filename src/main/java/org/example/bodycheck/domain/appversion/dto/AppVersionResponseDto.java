package org.example.bodycheck.domain.appversion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AppVersionResponseDto {

	@Builder
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AppUpdateStatusDto {
		private boolean needUpdate;
		private boolean forceUpdate;
		private String updateUrl;
		private String releaseNotes;
	}
}
