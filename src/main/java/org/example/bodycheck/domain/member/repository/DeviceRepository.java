package org.example.bodycheck.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.example.bodycheck.domain.member.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {

	List<Device> findByMember_Id(Long memberId);

	Optional<Device> findByDeviceId(String deviceId);
}
