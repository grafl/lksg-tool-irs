package grafl.msg.lksgtoolirs.controllers;

import grafl.msg.lksgtoolirs.models.AnalysisReport;
import grafl.msg.lksgtoolirs.models.Investigation;
import grafl.msg.lksgtoolirs.repositories.AnalysisReportRepository;
import grafl.msg.lksgtoolirs.repositories.AnalysisRepository;
import grafl.msg.lksgtoolirs.repositories.BpnRepository;
import grafl.msg.lksgtoolirs.repositories.InvestigationBpnRepository;
import grafl.msg.lksgtoolirs.requests.RegistrationRequest;
import grafl.msg.lksgtoolirs.services.InvestigationService;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.irs.component.Jobs;
import org.eclipse.tractusx.irs.component.enums.AspectType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/ess/bpn/investigations", produces = { APPLICATION_JSON_VALUE })
@RestController
@Slf4j
@CrossOrigin
public class IrsController {

    private final BpnRepository bpnRepository;
    private final InvestigationService investigationService;
    private final InvestigationBpnRepository investigationBpnRepository;
    private final AnalysisRepository analysisRepository;
    private final AnalysisReportRepository analysisReportRepository;

    public IrsController(BpnRepository bpnRepository,
                         InvestigationService investigationService,
                         InvestigationBpnRepository investigationBpnRepository,
                         AnalysisRepository analysisRepository,
                         AnalysisReportRepository analysisReportRepository) {
        this.bpnRepository = bpnRepository;
        this.investigationService = investigationService;
        this.investigationBpnRepository = investigationBpnRepository;
        this.analysisRepository = analysisRepository;
        this.analysisReportRepository = analysisReportRepository;
    }

    @GetMapping(value = "/")
    public List<Investigation> getAllInvestigations() {
        log.info("getAllInvestigations()");
        return this.investigationService.getAllInvestigations();
    }

    @PostMapping(value = "/")
    public String createResponseForRequest(
            @RequestBody RegistrationRequest requestBody) {
        log.info("createResponseForRequest() ---> {}", requestBody);
        return this.investigationService.storeInvestigation(requestBody);
    }

    @GetMapping(value = "/{id}")
    public Jobs getAnalysisForJobId(
            @PathVariable("id") String investigationId) {
        log.info("getAnalysisForJobId({})", investigationId);
        Jobs jobs = this.investigationService.getAnalysis(investigationId, null, null);
        return jobs;
    }

    @GetMapping(value = "/analyses/")
    public List<AnalysisReport> getAllInvestigationAnalyses() {
        // log.info("getAllInvestigationAnalyses()");
        return this.analysisReportRepository.findAll();
    }

    @PutMapping(value = "/{id}/analysis/{state}/{impacted}/{aspect_type}")
    public Jobs setAnalysisState(
            @PathVariable("id") String investigationId,
            @PathVariable("state") String state,
            @PathVariable("impacted") String impacted,
            @PathVariable("aspect_type") String aspect_type) {
        log.info("setAnalysisState(ID:{}, State:{}, AspectType:{})", investigationId, state, aspect_type);
        return this.investigationService.setAnalysisState(log, investigationId, state, impacted, AspectType.fromValue(aspect_type));
    }

    @DeleteMapping(value = "/")
    public void deleteEverything() {
        this.investigationBpnRepository.deleteAll();
        this.analysisRepository.deleteAll();
        this.investigationService.deleteAll();
        this.bpnRepository.deleteAll();
        log.info("Everything has been deleted!");
    }

}
