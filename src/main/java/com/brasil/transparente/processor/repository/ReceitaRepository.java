package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, String> {
}
