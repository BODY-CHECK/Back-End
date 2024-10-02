package org.example.bodycheck.domain.uuid.repository;

import org.example.bodycheck.domain.uuid.entity.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
