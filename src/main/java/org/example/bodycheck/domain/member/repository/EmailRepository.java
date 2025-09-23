package org.example.bodycheck.domain.member.repository;

import java.util.Optional;

import org.example.bodycheck.domain.member.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

	boolean existsByEmail(String email);

	Optional<Email> findByEmail(String email);
}
