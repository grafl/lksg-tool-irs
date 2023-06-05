package grafl.msg.lksgtoolirs.services;

import grafl.msg.lksgtoolirs.models.Analysis;
import grafl.msg.lksgtoolirs.models.Bpn;
import grafl.msg.lksgtoolirs.models.Investigation;
import grafl.msg.lksgtoolirs.models.InvestigationBpn;
import grafl.msg.lksgtoolirs.repositories.AnalysisRepository;
import grafl.msg.lksgtoolirs.repositories.BpnRepository;
import grafl.msg.lksgtoolirs.repositories.InvestigationBpnRepository;
import grafl.msg.lksgtoolirs.repositories.InvestigationRepository;
import grafl.msg.lksgtoolirs.requests.RegistrationRequest;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.irs.component.GlobalAssetIdentification;
import org.eclipse.tractusx.irs.component.Job;
import org.eclipse.tractusx.irs.component.Jobs;
import org.eclipse.tractusx.irs.component.Submodel;
import org.eclipse.tractusx.irs.component.enums.AspectType;
import org.eclipse.tractusx.irs.component.enums.JobState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class InvestigationService {

    private static final String SUPPLY_CHAIN_ASPECT_TYPE = "supply_chain_impacted";

    private final InvestigationRepository investigationRepository;
    private final BpnRepository bpnRepository;
    private final AnalysisRepository analysisRepository;
    private final InvestigationBpnRepository investigationBpnRepository;

    public InvestigationService(InvestigationRepository investigationRepository,
                                BpnRepository bpnRepository,
                                AnalysisRepository analysisRepository,
                                InvestigationBpnRepository investigationBpnRepository) {
        this.investigationRepository = investigationRepository;
        this.bpnRepository = bpnRepository;
        this.analysisRepository = analysisRepository;
        this.investigationBpnRepository = investigationBpnRepository;
    }

    public List<Investigation> getAllInvestigations() {
        List<Investigation> allInvestigations = this.investigationRepository.findAll();
        for(Investigation investigation : allInvestigations) {
            Optional<List<InvestigationBpn>> investigationBpns =
                    this.investigationBpnRepository.findByInvestigation(investigation.getId());
            if(!investigationBpns.isEmpty()) {
                List<Bpn> bpns = new ArrayList<>();
                for(InvestigationBpn investigationBpn : investigationBpns.get()) {
                    // log.info(investigationBpn.getBpn());
                    bpns.add(Bpn.builder().bpn(investigationBpn.getBpn()).build());
                }
                investigation.setIncidentBpns(bpns);
            }
        }
        return allInvestigations;
    }

    public List<Investigation> getAllBpns() {
        return this.investigationRepository.findAll();
    }

    public Optional<Investigation> getBpnById(String id) {
        return this.investigationRepository.findById(id);
    }

    public String storeInvestigation(RegistrationRequest requestBody) {
        Investigation investigation = Investigation.builder()
                .id(UUID.randomUUID().toString())
                .bomLifecycle(requestBody.getBomLifecycle())
                .callbackUrl(requestBody.getCallbackUrl())
                .globalAssetId(requestBody.getGlobalAssetId())
                // .incidentBpns(requestBody.bpns())
                .build();
        this.investigationRepository.save(investigation);
        for(Bpn bpn : requestBody.bpns()) {
            Optional<Bpn> bpnId = this.bpnRepository.findById(bpn.getBpn());
            if(bpnId.isEmpty()) {
                this.bpnRepository.save(Bpn.builder().bpn(bpn.getBpn()).build());
                this.investigationBpnRepository.save(InvestigationBpn.builder()
                        .investigation(investigation.getId())
                        .bpn(bpn.getBpn())
                        .build());
            }
        }
        Analysis analysis = Analysis.builder()
                .id(UUID.randomUUID().toString())
                .investigation(investigation.getId())
                .state(JobState.INITIAL.name())
                .build();
        this.analysisRepository.save(analysis);
        return investigation.getId();
    }

    @Transactional
    public Jobs getAnalysis(String investigationId, String impacted, AspectType aspectType) {
        Optional<Analysis> analysis = this.analysisRepository.findByInvestigation(investigationId);
        Optional<Investigation> investigation = this.investigationRepository.findById(investigationId);
        Optional<List<InvestigationBpn>> investigationBpns = this.investigationBpnRepository.findByInvestigation(investigationId);

        Collection<Submodel> submodels = new ArrayList<>();
        Map<String, Object> payload = new HashMap<>();
        Submodel submodel = null;
        if(impacted != null && aspectType != null) {
            payload.put(SUPPLY_CHAIN_ASPECT_TYPE, analysis.get().getImpacted());
            submodel = Submodel.builder()
                    .aspectType(aspectType.name())
                    .payload(payload).build();
            submodels.add(submodel);
        } else {
            if(analysis.get().getState().equals(JobState.COMPLETED.name())) {
                payload.put(SUPPLY_CHAIN_ASPECT_TYPE, analysis.get().getImpacted());
                submodel = Submodel.builder()
                        .aspectType(analysis.get().getAspect())
                        .payload(payload).build();
                submodels.add(submodel);
            }
        }
        Jobs jobs = Jobs.builder()
                .bpns(getSetOfBpns(investigationBpns.get()))
                .job(getJob(
                        analysis.get().getInvestigation(),
                        investigation.get().getGlobalAssetId(),
                        analysis.get().getState()
                ))
                .submodels(submodels)
                .build();
        return jobs;
    }

    @Transactional
    public Jobs setAnalysisState( org.slf4j.Logger log, String investigationId, String state, String impacted, AspectType aspectType) {
        Analysis new_analysis = this.analysisRepository.findByInvestigation(investigationId).get();
        new_analysis.setState(String.valueOf(JobState.value(state)));
        new_analysis.setImpacted(impacted);
        new_analysis.setAspect(aspectType.name());
        int seconds = getRandomNumber(3, 10);
        log.info("Waiting {} seconds ...", seconds);
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            // throw new RuntimeException(e);
        }
        this.analysisRepository.save(new_analysis);
        log.info("... done");
        return getAnalysis(investigationId, impacted, aspectType);
    }

    private Job getJob(String id, String globalAssetId, String state) {
        Job job = Job.builder()
                .id(UUID.fromString(id))
                .globalAssetId(GlobalAssetIdentification.of(globalAssetId))
                .state(JobState.value(state))
                .build();
        return job;
    }

    public void deleteAll() {
        this.investigationRepository.deleteAll();
    }

    private Set<org.eclipse.tractusx.irs.component.Bpn> getSetOfBpns(List<InvestigationBpn> incidentBpns) {
        Set<org.eclipse.tractusx.irs.component.Bpn> bpns = new HashSet<>();
        for(InvestigationBpn invbpn : incidentBpns) {
            bpns.add(org.eclipse.tractusx.irs.component.Bpn.of(invbpn.getBpn(), "OEM A"));
        }
        return bpns;
    }

    private int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
