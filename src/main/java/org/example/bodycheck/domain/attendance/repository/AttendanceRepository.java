package org.example.bodycheck.domain.attendance.repository;

import org.example.bodycheck.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
