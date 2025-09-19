package org.example.bodycheck.domain.member.converter;

import org.example.bodycheck.domain.member.dto.devicedto.DeviceRequestDto;
import org.example.bodycheck.domain.member.entity.Device;

public class DeviceConverter {

    public static Device toDevice(DeviceRequestDto.DeviceDto request) {
        return Device.builder()
                .fcmToken(request.getFcmToken())
                .deviceName(request.getDeviceName())
                .deviceOS(request.getDeviceOS())
                .deviceId(request.getDeviceId())
                .build();
    }
}
