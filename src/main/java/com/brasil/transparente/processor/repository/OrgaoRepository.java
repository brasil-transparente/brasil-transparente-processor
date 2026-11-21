package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.Orgao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {

}
