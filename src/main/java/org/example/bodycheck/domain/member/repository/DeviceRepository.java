package org.example.bodycheck.domain.member.repository;

import org.example.bodycheck.domain.member.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByMember_Id(Long memberId);
    Optional<Device> findByDevice_Id(String deviceId);
}
