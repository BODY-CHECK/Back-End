package org.example.bodycheck.domain.attendance.repository;

import org.example.bodycheck.domain.attendance.entity.Attendance;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByMemberAndDate(Member member, LocalDate date);
    List<Attendance> findAllByMemberAndDateBetween(Member member, LocalDate startDate, LocalDate endDate);

}
