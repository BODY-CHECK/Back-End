package org.example.bodycheck.domain.appversion.repository;

import java.util.Optional;

import org.example.bodycheck.domain.appversion.entity.AppVersion;
import org.example.bodycheck.domain.enums.DeviceOsType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
	Optional<AppVersion> findAppVersionByDeviceOS(DeviceOsType deviceOS);
}
