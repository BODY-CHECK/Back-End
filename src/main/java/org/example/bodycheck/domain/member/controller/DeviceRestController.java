package org.example.bodycheck.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.member.dto.DeviceDTO.DeviceRequestDTO;
import org.example.bodycheck.domain.member.service.DeviceService.DeviceCommandService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/devices")
public class DeviceRestController {

    private final DeviceCommandService deviceCommandService;

    @PostMapping
    public ApiResponse<String> updateDeviceInfo(@RequestBody DeviceRequestDTO.DeviceDTO request) {
        deviceCommandService.updateDevice(request);

        return ApiResponse.onSuccess("OK");
    }
}
