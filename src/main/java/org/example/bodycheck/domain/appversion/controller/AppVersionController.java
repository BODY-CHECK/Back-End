package org.example.bodycheck.domain.appversion.controller;

import org.example.bodycheck.common.apipayload.ApiResponse;
import org.example.bodycheck.domain.appversion.dto.AppVersionRequestDto;
import org.example.bodycheck.domain.appversion.dto.AppVersionResponseDto;
import org.example.bodycheck.domain.appversion.service.AppVersionService;
import org.example.bodycheck.domain.enums.DeviceOsType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/versions")
public class AppVersionController {

	private final AppVersionService appVersionService;

	@GetMapping("/status")
	public ApiResponse<AppVersionResponseDto.AppUpdateStatusDto> getUpdateStatus(@RequestParam DeviceOsType deviceOS,
		@RequestParam String currentVersion) {
		AppVersionResponseDto.AppUpdateStatusDto appUpdateStatusDto = appVersionService.getUpdateStatus(deviceOS,
			currentVersion);
		return ApiResponse.onSuccess(appUpdateStatusDto);
	}

	@PatchMapping("/{deviceOS}")
	public ApiResponse<String> updateAppVersion(@PathVariable DeviceOsType deviceOS,
		@RequestBody AppVersionRequestDto.AppVersionDto request) {
		appVersionService.updateAppVersion(deviceOS, request);
		return ApiResponse.onSuccess("OK");
	}
}
