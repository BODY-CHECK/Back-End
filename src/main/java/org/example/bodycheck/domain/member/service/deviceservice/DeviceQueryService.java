package org.example.bodycheck.domain.member.service.deviceservice;

import org.example.bodycheck.domain.member.entity.Device;

import java.util.List;

public interface DeviceQueryService {

    List<Device> getDeviceList(Long memberId);
}
