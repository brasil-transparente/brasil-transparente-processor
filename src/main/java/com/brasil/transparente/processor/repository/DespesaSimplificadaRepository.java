package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.DespesaSimplificada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DespesaSimplificadaRepository extends JpaRepository<DespesaSimplificada, String> {

    @Query("SELECT COALESCE(SUM(ds.totalValue), 0) FROM DespesaSimplificada ds")
    double sumTotalValue();


}
