package org.example.bodycheck.domain.member.service.DeviceService;

import org.example.bodycheck.domain.member.dto.DeviceDTO.DeviceRequestDTO;

public interface DeviceCommandService {

    void updateDevice(DeviceRequestDTO.DeviceDTO request);
}
