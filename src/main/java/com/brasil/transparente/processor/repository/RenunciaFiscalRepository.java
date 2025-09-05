package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.RenunciaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenunciaFiscalRepository extends JpaRepository<RenunciaFiscal, Long> {
}