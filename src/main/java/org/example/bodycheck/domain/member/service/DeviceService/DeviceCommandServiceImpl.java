package org.example.bodycheck.domain.member.service.DeviceService;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.member.converter.DeviceConverter;
import org.example.bodycheck.domain.member.dto.DeviceDTO.DeviceRequestDTO;
import org.example.bodycheck.domain.member.entity.Device;
import org.example.bodycheck.domain.member.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;

    @Override
    public void updateDevice(DeviceRequestDTO.DeviceDTO request) {
        Device device = deviceRepository.findByDevice_Id(request.getDeviceId()).orElseGet(() -> DeviceConverter.toDevice(request));

        device.updateFcmToken(request.getFcmToken());

        deviceRepository.save(device);
    }
}
