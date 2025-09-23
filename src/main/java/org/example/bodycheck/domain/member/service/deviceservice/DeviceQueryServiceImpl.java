package org.example.bodycheck.domain.member.service.deviceservice;

import java.util.List;

import org.example.bodycheck.domain.member.entity.Device;
import org.example.bodycheck.domain.member.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceQueryServiceImpl implements DeviceQueryService {

	private final DeviceRepository deviceRepository;

	@Override
	public List<Device> getDeviceList(Long memberId) {
		return deviceRepository.findByMember_Id(memberId);
	}
}
