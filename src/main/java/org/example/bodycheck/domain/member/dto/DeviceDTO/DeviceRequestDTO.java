package org.example.bodycheck.domain.member.dto.DeviceDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.example.bodycheck.domain.enums.DeviceOSType;

public class DeviceRequestDTO {

    @Getter
    public static class DeviceDTO {
        private String fcmToken;
        private String deviceName;
        private DeviceOSType deviceOS;
        private String deviceId;
    }
}
