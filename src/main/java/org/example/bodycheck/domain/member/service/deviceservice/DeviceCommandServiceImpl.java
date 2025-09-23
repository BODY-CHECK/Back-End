package org.example.bodycheck.domain.member.service.deviceservice;

import org.example.bodycheck.domain.member.converter.DeviceConverter;
import org.example.bodycheck.domain.member.dto.devicedto.DeviceRequestDto;
import org.example.bodycheck.domain.member.entity.Device;
import org.example.bodycheck.domain.member.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceCommandServiceImpl implements DeviceCommandService {

	private final DeviceRepository deviceRepository;

	@Override
	public void updateDevice(DeviceRequestDto.DeviceDto request) {
		Device device = deviceRepository.findByDeviceId(request.getDeviceId())
			.orElseGet(() -> DeviceConverter.toDevice(request));

		device.updateFcmToken(request.getFcmToken());

		deviceRepository.save(device);
	}
}
