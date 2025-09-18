package org.example.bodycheck.domain.member.dto.DeviceDTO;

import lombok.Getter;

public class DeviceRequestDTO {

    @Getter
    public static class DeviceDTO {
        private String fcmToken;
        private String deviceName;
        private String deviceOS;
        private String deviceId;
    }
}
