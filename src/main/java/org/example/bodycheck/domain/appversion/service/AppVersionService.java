package org.example.bodycheck.domain.appversion.service;

import org.example.bodycheck.common.apipayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.appversion.dto.AppVersionRequestDto;
import org.example.bodycheck.domain.appversion.dto.AppVersionResponseDto;
import org.example.bodycheck.domain.appversion.entity.AppVersion;
import org.example.bodycheck.domain.appversion.repository.AppVersionRepository;
import org.example.bodycheck.domain.enums.DeviceOsType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppVersionService {

	private final AppVersionRepository appVersionRepository;

	@Transactional(readOnly = true)
	public AppVersionResponseDto.AppUpdateStatusDto getUpdateStatus(DeviceOsType deviceOS, String currentVersion) {
		AppVersion appVersion = appVersionRepository.findAppVersionByDeviceOS(deviceOS)
			.orElseThrow(() -> new GeneralHandler(
				ErrorStatus.OS_NOT_FOUND));

		boolean needUpdate = isUpdateRequired(currentVersion, appVersion.getMinSupportedVersion());

		return AppVersionResponseDto.AppUpdateStatusDto.builder()
			.needUpdate(needUpdate)
			.forceUpdate(false)
			.updateUrl(appVersion.getUpdateUrl())
			.releaseNotes(appVersion.getReleaseNotes())
			.build();
	}

	@Transactional
	public void updateAppVersion(DeviceOsType deviceOS, AppVersionRequestDto.AppVersionDto request) {
		AppVersion appVersion = appVersionRepository.findAppVersionByDeviceOS(deviceOS)
			.orElseThrow(() -> new GeneralHandler(
				ErrorStatus.OS_NOT_FOUND));

		appVersion.updateAppVersion(request.getLatestVersion(), request.getMinSupportedVersion(),
			request.getReleaseNotes());
	}

	private boolean isUpdateRequired(String currentVersion, String minSupportedVersion) {
		String[] arr1 = currentVersion.split("\\.");
		String[] arr2 = minSupportedVersion.split("\\.");

		int length = Math.max(arr1.length, arr2.length);
		for (int i = 0; i < length; i++) {
			int num1 = i < arr1.length ? Integer.parseInt(arr1[i]) : 0;
			int num2 = i < arr2.length ? Integer.parseInt(arr2[i]) : 0;

			if (num1 < num2) {
				return true;
			}
			if (num1 > num2) {
				return false;
			}
		}
		return false;
	}
}
