package com.brasil.transparente.processor.repository;

import com.brasil.transparente.processor.entity.ElementoDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementoDespesaRepository extends JpaRepository<ElementoDespesa, String> {

    @Query("SELECT ed FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio mini " +
            "JOIN mini.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE ed.nameElementoDespesa = :nameElementoDespesa " +
            "AND uf.unidadeFederativaId = :unidadeFederativaId")
    ElementoDespesa findByNameElementoDespesaAndUnidadeFederativa(
            @Param("nameElementoDespesa") String nameElementoDespesa,
            @Param("unidadeFederativaId") Long unidadeFederativaId
    );


    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio mini " +
            "JOIN mini.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE (ed.nameElementoDespesa IN :elementoDespesaNameList " +
            "OR org.nameOrgao = :nameOrgao) " +
            "AND uf.unidadeFederativaId = :unidadeFederativaId")
    double findByElementoDespesaNameListOrNameOrgaoAndUnidadeFederativa(
            @Param("elementoDespesaNameList") List<String> elementoDespesaNameList,
            @Param("nameOrgao") String nameOrgao,
            @Param("unidadeFederativaId") Long unidadeFederativaId
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio mini " +
            "JOIN mini.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE mini.nameMinisterio = :nameMinisterio " +
            "AND ed.nameElementoDespesa IN :elementoDespesaNameList " +
            "AND uf.unidadeFederativaId = :unidadeFederativaId")
    double findByMinisterioAndElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
            @Param("nameMinisterio") String nameMinisterio,
            @Param("elementoDespesaNameList") List<String> elementoDespesaNameList,
            @Param("unidadeFederativaId") Long unidadeFederativaId
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio mini " +
            "JOIN mini.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE mini.nameMinisterio = :nameMinisterio " +
            "AND ed.nameElementoDespesa NOT IN :elementoDespesaNameList " +
            "AND uf.unidadeFederativaId = :unidadeFederativaId")
    double findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
            @Param("nameMinisterio") String nameMinisterio,
            @Param("elementoDespesaNameList") List<String> elementoDespesaNameList,
            @Param("unidadeFederativaId") Long unidadeFederativaId
    );


    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio mini " +
            "JOIN mini.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE mini.nameMinisterio = :nameMinisterio " +
            "AND ed.nameElementoDespesa NOT IN :elementoDespesaNameListNot " +
            "AND ed.nameElementoDespesa IN :elementoDespesaNameListIn " +
            "AND uf.unidadeFederativaId = :unidadeFederativaId")
    double findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndInElementoDespesaNameListAndUnidadeFederativa(
            @Param("nameMinisterio") String nameMinisterio,
            @Param("elementoDespesaNameListNot") List<String> elementoDespesaNameListNot,
            @Param("elementoDespesaNameListIn") List<String> elementoDespesaNameListIn,
            @Param("unidadeFederativaId") Long unidadeFederativaId
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio mini " +
            "JOIN mini.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE pod.namePoder = :namePoder " +
            "AND ed.nameElementoDespesa NOT IN :elementoDespesaNameList " +
            "AND uf.unidadeFederativaId = :unidadeFederativaId")
    double findByPoderAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
            @Param("namePoder") String namePoder,
            @Param("elementoDespesaNameList") List<String> elementoDespesaNameList,
            @Param("unidadeFederativaId") Long unidadeFederativaId
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio mini " +
            "JOIN mini.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE mini.nameMinisterio = :nameMinisterio " +
            "AND uf.unidadeFederativaId = :unidadeFederativaId")
    double findByMinisterioAndUnidadeFederativa(
            @Param("nameMinisterio") String nameMinisterio,
            @Param("unidadeFederativaId") Long unidadeFederativaId
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio min " +
            "JOIN min.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE uf.unidadeFederativaId = :unidadeFederativaId " +
            "AND (" +
            "LOWER(ed.nameElementoDespesa) LIKE %:term1% " +
            "OR LOWER(ed.nameElementoDespesa) LIKE %:term2% " +
            "OR LOWER(ed.nameElementoDespesa) LIKE %:term3% " +
            "OR org.nameOrgao = :nameOrgao" +
            ")")
    double findDespesasPrevidenciaByEstado(
            @Param("unidadeFederativaId") Long unidadeFederativaId,
            @Param("term1") String term1,
            @Param("term2") String term2,
            @Param("term3") String term3,
            @Param("nameOrgao") String nameOrgao
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio min " +
            "JOIN min.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE uf.unidadeFederativaId = :unidadeFederativaId " +
            "AND (min.nameMinisterio = :nameMinisterio1 OR min.nameMinisterio = :nameMinisterio2)")
    double findDespesasSegurancaByEstado(
            @Param("unidadeFederativaId") Long unidadeFederativaId,
            @Param("nameMinisterio1") String nameMinisterio1,
            @Param("nameMinisterio2") String nameMinisterio2
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio min " +
            "JOIN min.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE uf.unidadeFederativaId = :unidadeFederativaId " +
            "AND min.nameMinisterio = :nameMinisterio")
    double findDespesasSaudeByEstado(
            @Param("unidadeFederativaId") Long unidadeFederativaId,
            @Param("nameMinisterio") String nameMinisterio
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio min " +
            "JOIN min.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE uf.unidadeFederativaId = :unidadeFederativaId " +
            "AND min.nameMinisterio = :nameMinisterio")
    double findDespesasEducacaoByEstado(
            @Param("unidadeFederativaId") Long unidadeFederativaId,
            @Param("nameMinisterio") String nameMinisterio
    );

    @Query("SELECT SUM(ed.totalValueSpent) FROM ElementoDespesa ed " +
            "JOIN ed.unidadeGestora ug " +
            "JOIN ug.orgao org " +
            "JOIN org.ministerio min " +
            "JOIN min.poder pod " +
            "JOIN pod.unidadeFederativa uf " +
            "WHERE uf.unidadeFederativaId = :unidadeFederativaId " +
            "AND pod.namePoder = :namePoder " +
            "AND LOWER(ed.nameElementoDespesa) NOT LIKE %:term1% " +
            "AND LOWER(ed.nameElementoDespesa) NOT LIKE %:term2% " +
            "AND LOWER(ed.nameElementoDespesa) NOT LIKE %:term3%")
    double findDespesasJudiciarioByEstado(
            @Param("unidadeFederativaId") Long unidadeFederativaId,
            @Param("namePoder") String namePoder,
            @Param("term1") String term1,
            @Param("term2") String term2,
            @Param("term3") String term3
    );

}
