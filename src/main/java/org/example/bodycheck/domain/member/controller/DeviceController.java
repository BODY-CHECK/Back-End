package org.example.bodycheck.domain.member.controller;

import org.example.bodycheck.common.apipayload.ApiResponse;
import org.example.bodycheck.domain.member.dto.devicedto.DeviceRequestDto;
import org.example.bodycheck.domain.member.service.deviceservice.DeviceCommandService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/devices")
public class DeviceController {

	private final DeviceCommandService deviceCommandService;

	@PutMapping
	public ApiResponse<String> updateDeviceInfo(@RequestBody DeviceRequestDto.DeviceDto request) {
		deviceCommandService.updateDevice(request);
		return ApiResponse.onSuccess("OK");
	}
}
