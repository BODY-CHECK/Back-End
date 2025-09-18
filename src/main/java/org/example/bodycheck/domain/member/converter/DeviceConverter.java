package org.example.bodycheck.domain.member.converter;

import org.example.bodycheck.domain.member.dto.DeviceDTO.DeviceRequestDTO;
import org.example.bodycheck.domain.member.entity.Device;

public class DeviceConverter {

    public static Device toDevice(DeviceRequestDTO.DeviceDTO request) {
        return Device.builder()
                .fcmToken(request.getFcmToken())
                .deviceName(request.getDeviceName())
                .deviceOS(request.getDeviceOS())
                .deviceId(request.getDeviceId())
                .build();
    }
}
