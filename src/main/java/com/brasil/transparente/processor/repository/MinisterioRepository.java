package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.Ministerio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinisterioRepository extends JpaRepository<Ministerio, Long> {

}
