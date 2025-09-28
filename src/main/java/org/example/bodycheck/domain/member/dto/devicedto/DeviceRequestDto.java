package org.example.bodycheck.domain.member.dto.devicedto;

import org.example.bodycheck.domain.enums.DeviceOsType;

import lombok.Getter;

public class DeviceRequestDto {

	@Getter
	public static class DeviceDto {
		private String fcmToken;
		private String deviceName;
		private DeviceOsType deviceOS;
		private String deviceId;
	}
}
