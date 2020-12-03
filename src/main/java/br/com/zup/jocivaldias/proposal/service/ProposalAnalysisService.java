package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.ProposalAnalysisRequest;
import br.com.zup.jocivaldias.proposal.dto.response.ProposalAnalysisResponse;
import br.com.zup.jocivaldias.proposal.entity.Proposal;
import br.com.zup.jocivaldias.proposal.entity.enums.ProposalStatus;
import br.com.zup.jocivaldias.proposal.repository.ProposalRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class ProposalAnalysisService {

    private ProposalAnalysis proposalAnalysisClient;
    private ProposalRepository proposalRepository;
    private TransactionTemplate transactionTemplate;

    private final Logger logger = LoggerFactory.getLogger(ProposalAnalysisService.class);

    public ProposalAnalysisService(ProposalAnalysis proposalAnalysisClient,
                                   ProposalRepository proposalRepository,
                                   TransactionTemplate transactionTemplate) {
        this.proposalAnalysisClient = proposalAnalysisClient;
        this.proposalRepository = proposalRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Scheduled(fixedDelay = 6000)
    public void analyzesProposal(){
        List<Proposal> pendingAnalysisProposals = proposalRepository.findByStatus(ProposalStatus.OPENED);
        for(Proposal proposal : pendingAnalysisProposals) {

            ProposalAnalysisRequest proposalAnalysisRequest = new ProposalAnalysisRequest(proposal.getDocumentNumber(),
                    proposal.getName(),
                    proposal.getId().toString());

            ProposalAnalysisResponse proposalAnalysisResponse = null;
            try {
                proposalAnalysisResponse = proposalAnalysisClient.requestAnalysis(proposalAnalysisRequest);

                if(proposalAnalysisResponse.getResultadoSolicitacao().equalsIgnoreCase("SEM_RESTRICAO"))
                    proposal.setStatus(ProposalStatus.ELIGIBLE);

            } catch (FeignException.UnprocessableEntity unprocessableEntity) {
                try {
                    proposalAnalysisResponse = new ObjectMapper().readValue(unprocessableEntity.contentUTF8(), ProposalAnalysisResponse.class);
                    if(proposalAnalysisResponse.getResultadoSolicitacao().equalsIgnoreCase("COM_RESTRICAO")){
                        proposal.setStatus(ProposalStatus.NOT_ELIGIBLE);
                    }
                } catch (JsonProcessingException e) {
                    logger.error("Error in Proposal Analysis API - JSON parsing failed.");
                }
            } catch (FeignException feignException) {
                logger.error("Error in Proposal Analysis API - Status code: {}, Body: {}, Message: {}",
                        feignException.status(),
                        feignException.contentUTF8(),
                        feignException.getMessage());
            } finally {
                if(proposal.getStatus().equals(ProposalStatus.ELIGIBLE) ||
                        proposal.getStatus().equals(ProposalStatus.NOT_ELIGIBLE)) {
                    transactionTemplate.execute(status -> {
                        proposalRepository.save(proposal);
                        return true;
                    });
                }
            }
        }
    }

}
