package org.example.bodycheck.domain.member.service.DeviceService;

import org.example.bodycheck.domain.member.entity.Device;

import java.util.List;

public interface DeviceQueryService {

    List<Device> getDeviceList(Long memberId);
}
