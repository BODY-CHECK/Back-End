package org.example.bodycheck.domain.member.service.deviceservice;

import java.util.List;

import org.example.bodycheck.domain.member.entity.Device;

public interface DeviceQueryService {

	List<Device> getDeviceList(Long memberId);
}
