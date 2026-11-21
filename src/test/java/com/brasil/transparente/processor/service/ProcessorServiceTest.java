package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.repository.*;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.executivo.ExecutivoService;
import com.brasil.transparente.processor.service.judiciario.JudiciarioService;
import com.brasil.transparente.processor.service.legislativo.LegislativoService;
import com.brasil.transparente.processor.service.orgaos.OrgaosAutonomosService;
import com.brasil.transparente.processor.service.simplificada.SimplificadaService;
import com.brasil.transparente.processor.util.NameCorrectorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessorServiceTest {

    @Mock
    private ExecutivoService executivoService;
    @Mock
    private JudiciarioService judiciarioService;
    @Mock
    private LegislativoService legislativoGeneratorService;
    @Mock
    private SimplificadaService orchestrationService;
    @Mock
    private OrgaosAutonomosService orgaosAutonomosGeneratorService;
    @Mock
    private CreateEntityService createEntityService;
    @Mock
    private NameCorrectorService nameCorrectorService;
    @Mock
    private PoderRepository poderRepository;
    @Mock
    private MinisterioRepository ministerioRepository;
    @Mock
    private OrgaoRepository orgaoRepository;
    @Mock
    private UnidadeGestoraRepository unidadeGestoraRepository;
    @Mock
    private ElementoDespesaRepository elementoDespesaRepository;
    @Mock
    private DespesaSimplificadaRepository despesaSimplificadaRepository;
    @Mock
    private ProcessExpensesService processExpensesService;

    @InjectMocks
    private ProcessorService processorService;

    @Test
    void generateCompleteReportService_shouldCoordinateFullProcessingPipeline() {
        String year = "2024";
        Poder exec = new Poder("Executivo");
        Poder jud = new Poder("Judiciário");
        Poder leg = new Poder("Legislativo");
        Poder org = new Poder("Órgãos Autônomos");

        when(executivoService.generateExecutivoBranch(year)).thenReturn(exec);
        when(judiciarioService.generateJudiciarioBranch(year)).thenReturn(jud);
        when(legislativoGeneratorService.generateLegislativoBranch(year)).thenReturn(leg);
        when(orgaosAutonomosGeneratorService.generateOrgaoIndependentesBranch(year)).thenReturn(org);
        when(processExpensesService.aggregateTotalExpense(anyList())).thenReturn(5000.0);

        processorService.generateCompleteReportService(year);

        verify(despesaSimplificadaRepository).deleteAllInBatch();
        verify(elementoDespesaRepository).deleteAllInBatch();
        verify(unidadeGestoraRepository).deleteAllInBatch();
        verify(orgaoRepository).deleteAllInBatch();
        verify(ministerioRepository).deleteAllInBatch();
        verify(poderRepository).deleteAllInBatch();

        verify(executivoService).generateExecutivoBranch(year);
        verify(judiciarioService).generateJudiciarioBranch(year);
        verify(legislativoGeneratorService).generateLegislativoBranch(year);
        verify(orgaosAutonomosGeneratorService).generateOrgaoIndependentesBranch(year);

        verify(createEntityService).setRelationships(anyList());
        verify(processExpensesService).aggregateTotalExpense(anyList());
        verify(processExpensesService).removeNegativeOrZeroExpenses(anyList());
        verify(processExpensesService).setTotalPercentages(anyList(), eq(5000.0));
        verify(nameCorrectorService).refactorNames(anyList());

        verify(poderRepository).saveAll(anyList());
        verify(orchestrationService).generateSimplifiedReportUniao(5000.0);
    }
}
