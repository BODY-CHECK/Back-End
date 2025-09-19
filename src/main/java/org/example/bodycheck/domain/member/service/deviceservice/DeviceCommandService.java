package org.example.bodycheck.domain.member.service.deviceservice;

import org.example.bodycheck.domain.member.dto.devicedto.DeviceRequestDto;

public interface DeviceCommandService {

    void updateDevice(DeviceRequestDto.DeviceDto request);
}
