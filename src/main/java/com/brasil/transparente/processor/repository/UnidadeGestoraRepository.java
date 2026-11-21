package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.UnidadeGestora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeGestoraRepository extends JpaRepository<UnidadeGestora, Long> {

}
