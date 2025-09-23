package org.example.bodycheck.domain.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.bodycheck.domain.attendance.entity.Attendance;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	boolean existsByMemberAndDate(Member member, LocalDate date);

	Optional<Attendance> findByMemberAndDate(Member member, LocalDate date);

	List<Attendance> findAllByMemberAndDateBetween(Member member, LocalDate startDate, LocalDate endDate);
}
