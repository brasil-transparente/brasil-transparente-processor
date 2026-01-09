package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.Repasse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepasseRepository extends JpaRepository<Repasse, String> {

}
