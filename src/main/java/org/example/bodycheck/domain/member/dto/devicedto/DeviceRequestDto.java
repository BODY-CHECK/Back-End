package org.example.bodycheck.domain.member.dto.devicedto;

import lombok.Getter;
import org.example.bodycheck.domain.enums.DeviceOSType;

public class DeviceRequestDto {

    @Getter
    public static class DeviceDto {
        private String fcmToken;
        private String deviceName;
        private DeviceOSType deviceOS;
        private String deviceId;
    }
}
