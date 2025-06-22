package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.DespesaSimplificada;
import com.brasil.transparente.processor.entity.ElementoDespesa;
import com.brasil.transparente.processor.repository.*;
import com.brasil.transparente.processor.util.constants.SimplifiedConstants;
import com.brasil.transparente.processor.util.constants.UnidadesFederativasConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DespesaSimplificadaGeneratorService {

    private final ElementoDespesaRepository elementoDespesaRepository;
    private final UnidadeFederativaRepository unidadeFederativaRepository;
    private final DespesaSimplificadaRepository despesaSimplificadaRepository;
    private double allMoneySpent;

    public void generateSimplifiedReportUniao() {
        log.info("Gerando estrutura simplificada - União");
        allMoneySpent = unidadeFederativaRepository.findById("1").get().getTotalValueSpent();
        calculateAndSaveUniao1();
        calculateAndSaveUniao2();
        calculateAndSaveUniao3();
        calculateAndSaveUniao4();
        calculateAndSaveUniao5();
        calculateAndSaveUniao6();
        calculateAndSaveUniao7();
        calculateAndSaveUniao8();
        calculateAndSaveUniao9();
        calculateAndSaveUniaoOutros();
    }

    public void generateSimplifiedReportRS() {
        log.info("Gerando estrutura simplificada - RS");
        allMoneySpent = unidadeFederativaRepository.findById("2").get().getTotalValueSpent();
        calculateAndSaveRS1();
        calculateAndSaveRS2();
        calculateAndSaveRS3();
        calculateAndSaveRS4();
        calculateAndSaveRS5();
        calculateAndSaveRSOutros();
    }

    public void generateSimplifiedReportBA() {
        log.info("Gerando estrutura simplificada - BA");
        allMoneySpent = unidadeFederativaRepository.findById("3").get().getTotalValueSpent();
        calculateAndSaveBA1();
        calculateAndSaveBA2();
        calculateAndSaveBA3();
        calculateAndSaveBA4();
        calculateAndSaveBA5();
        calculateAndSaveBA6();
        calculateAndSaveBA7();
        calculateAndSaveBAOutros();
    }

    private void calculateAndSaveUniao1() {
        double totalValueSpent = elementoDespesaRepository.findByElementoDespesaNameListOrNameOrgaoAndUnidadeFederativa
                (SimplifiedConstants.DESPESAS_APOSENTADORIAS,
                        SimplifiedConstants.FRGPS,
                        UnidadesFederativasConstants.UN_ID);
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.APOSENTADORIAS_PENSOES,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniao2() {
        double totalValueSpent = elementoDespesaRepository.findByMinisterioAndElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                SimplifiedConstants.MINISTERIO_FAZENDA,
                SimplifiedConstants.DESPESAS_DIVIDA_PUBLICA,
                UnidadesFederativasConstants.UN_ID);
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.JUROS_DIVIDA_PUBLICA,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniao3() {
        double totalValueSpent = elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                SimplifiedConstants.MINISTERIO_SAUDE,
                SimplifiedConstants.DESPESAS_APOSENTADORIAS,
                UnidadesFederativasConstants.UN_ID
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.SAUDE,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniao4() {
        double totalValueSpent = elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndInElementoDespesaNameListAndUnidadeFederativa(
                SimplifiedConstants.MINISTERIO_ASSISTENCIA_SOCIAL,
                SimplifiedConstants.DESPESAS_APOSENTADORIAS,
                SimplifiedConstants.AUXILIOS,
                UnidadesFederativasConstants.UN_ID
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.TRANSFERENCIA_RENDA,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniao5() {
        double totalValueSpent = elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                SimplifiedConstants.MINISTERIO_EDUCACAO,
                SimplifiedConstants.DESPESAS_APOSENTADORIAS,
                UnidadesFederativasConstants.UN_ID
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.EDUCACAO,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniao6() {
        double totalValueSpent = elementoDespesaRepository.findByMinisterioAndUnidadeFederativa(
                SimplifiedConstants.PRECATORIOS_RPVS,
                UnidadesFederativasConstants.UN_ID);
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.PRECATORIOS_RPVS,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniao7() {
        double totalValueSpent = elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                SimplifiedConstants.MINISTERIO_DEFESA,
                SimplifiedConstants.DESPESAS_APOSENTADORIAS,
                UnidadesFederativasConstants.UN_ID);
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.DEFESA,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniao8() {
        ElementoDespesa elementoDespesa = elementoDespesaRepository.findByNameElementoDespesaAndUnidadeFederativa(
                SimplifiedConstants.SEGURO_DESEMPREGO_ABONO_SALARIAL,
                UnidadesFederativasConstants.UN_ID);
        double percentageOfTotal = getPercentageOfTotal(elementoDespesa.getTotalValueSpent());
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.BENEFICIOS_TRABALHISTAS,
                elementoDespesa.getTotalValueSpent(),
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniao9() {
        double totalValueSpent = elementoDespesaRepository.findByPoderAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                SimplifiedConstants.PODER_JUDICIARIO,
                SimplifiedConstants.DESPESAS_APOSENTADORIAS,
                UnidadesFederativasConstants.UN_ID);
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.PODER_JUDICIARIO_FEDERAL,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
                );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveUniaoOutros() {
        double gastosSimplificado = despesaSimplificadaRepository.sumTotalValueByUnidadeFederativa(UnidadesFederativasConstants.UN_ID);
        double sobragastos = allMoneySpent - gastosSimplificado;
        double percentageOfTotal = getPercentageOfTotal(sobragastos);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.OUTROS,
                sobragastos,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID
                );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveRS1() {
        double totalValueSpent = elementoDespesaRepository.findDespesasPrevidenciaRS(
                UnidadesFederativasConstants.RS_ID,
                SimplifiedConstants.TERMO_APOSENTADORIA1,
                SimplifiedConstants.TERMO_APOSENTADORIA2,
                SimplifiedConstants.TERMO_APOSENTADORIA3,
                SimplifiedConstants.INSTITUTO_PREVIDENCIA_RS
                );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.APOSENTADORIAS_PENSOES,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveRS2() {
        double totalValueSpent = elementoDespesaRepository.findDespesasSegurancaByEstado(
                UnidadesFederativasConstants.RS_ID,
                SimplifiedConstants.SECRETARIA_SEGURANCA,
                SimplifiedConstants.SECRETARIA_PENAL_SOCIOEDUCATIVO_RS
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.SEGURANCA,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveRS3() {
        double totalValueSpent = elementoDespesaRepository.findDespesasSaudeByEstado(
                UnidadesFederativasConstants.RS_ID,
                SimplifiedConstants.SECRETARIA_SAUDE
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.SAUDE,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveRS4() {
        double totalValueSpent = elementoDespesaRepository.findDespesasEducacaoByEstado(
                UnidadesFederativasConstants.RS_ID,
                SimplifiedConstants.SECRETARIA_EDUCACAO
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.EDUCACAO,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveRS5() {
        double totalValueSpent = elementoDespesaRepository.findDespesasJudiciarioByEstado(
                UnidadesFederativasConstants.RS_ID,
                SimplifiedConstants.PODER_JUDICIARIO,
                SimplifiedConstants.TERMO_APOSENTADORIA1,
                SimplifiedConstants.TERMO_APOSENTADORIA2,
                SimplifiedConstants.TERMO_APOSENTADORIA3
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.PODER_JUDICIARIO_ESTADUAL,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID
                );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveRSOutros() {
        double gastosSimplificado = despesaSimplificadaRepository.sumTotalValueByUnidadeFederativa(UnidadesFederativasConstants.RS_ID);
        double sobragastos = allMoneySpent - gastosSimplificado;
        double percentageOfTotal = getPercentageOfTotal(sobragastos);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.OUTROS,
                sobragastos,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveBA1() {
        double totalValueSpent = elementoDespesaRepository.findDespesasPrevidenciaBA(
                UnidadesFederativasConstants.BA_ID,
                SimplifiedConstants.TERMO_APOSENTADORIA1,
                SimplifiedConstants.TERMO_APOSENTADORIA2,
                SimplifiedConstants.TERMO_APOSENTADORIA3,
                SimplifiedConstants.FUNDO_PREVIDENCIA_BAHIA_1,
                SimplifiedConstants.FUNDO_PREVIDENCIA_BAHIA_2
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.APOSENTADORIAS_PENSOES,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.BA_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveBA2() {
        double totalValueSpent = elementoDespesaRepository.findDespesasEducacaoByEstado(
                UnidadesFederativasConstants.BA_ID,
                SimplifiedConstants.SECRETARIA_EDUCACAO
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.EDUCACAO,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.BA_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveBA3() {
        double totalValueSpent = elementoDespesaRepository.findDespesasSaudeByEstado(
                UnidadesFederativasConstants.BA_ID,
                SimplifiedConstants.SECRETARIA_SAUDE
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.SAUDE,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.BA_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveBA4() {
        double totalValueSpent = elementoDespesaRepository.findDespesasSegurancaByEstado(
                UnidadesFederativasConstants.RS_ID,
                SimplifiedConstants.SECRETARIA_SEGURANCA,
                SimplifiedConstants.SECRETARIA_PENAL_SOCIOEDUCATIVO_BA
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.SEGURANCA,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.BA_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveBA5() {
        double totalValueSpent = elementoDespesaRepository.findDespesasJudiciarioByEstado(
                UnidadesFederativasConstants.BA_ID,
                SimplifiedConstants.PODER_JUDICIARIO,
                SimplifiedConstants.TERMO_APOSENTADORIA1,
                SimplifiedConstants.TERMO_APOSENTADORIA2,
                SimplifiedConstants.TERMO_APOSENTADORIA3
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.PODER_JUDICIARIO_ESTADUAL,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.BA_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveBA6() {
        double totalValueSpent = elementoDespesaRepository.findDespesasDividaByEstado(
                UnidadesFederativasConstants.BA_ID,
                SimplifiedConstants.ENCARGOS_GERAIS,
                SimplifiedConstants.DESPESAS_DIVIDA_PUBLICA
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.DIVIDA_PUBLICA,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.BA_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveBA7() {
        double totalValueSpent = elementoDespesaRepository.findDespesasInfraestruturaByEstado(
                UnidadesFederativasConstants.BA_ID,
                SimplifiedConstants.SECRETARIAS_INFRAESTRUTURA
        );
        double percentageOfTotal = getPercentageOfTotal(totalValueSpent);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.INFRAESTRUTURA,
                totalValueSpent,
                percentageOfTotal,
                UnidadesFederativasConstants.BA_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private void calculateAndSaveBAOutros() {
        double gastosSimplificado = despesaSimplificadaRepository.sumTotalValueByUnidadeFederativa(UnidadesFederativasConstants.BA_ID);
        double sobragastos = allMoneySpent - gastosSimplificado;
        double percentageOfTotal = getPercentageOfTotal(sobragastos);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                SimplifiedConstants.OUTROS,
                sobragastos,
                percentageOfTotal,
                UnidadesFederativasConstants.BA_ID
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    private double getPercentageOfTotal(double totalValueSpent) {
        return (totalValueSpent/allMoneySpent)*100;
    }

}
