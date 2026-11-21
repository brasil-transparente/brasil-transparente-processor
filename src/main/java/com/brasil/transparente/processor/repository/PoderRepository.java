package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.Poder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoderRepository extends JpaRepository<Poder, Long> {
}
