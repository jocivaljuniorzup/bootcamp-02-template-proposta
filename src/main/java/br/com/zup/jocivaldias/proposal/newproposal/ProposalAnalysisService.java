package br.com.zup.jocivaldias.proposal.newproposal;

import br.com.zup.jocivaldias.proposal.newproposal.dto.request.ProposalAnalysisRequest;
import br.com.zup.jocivaldias.proposal.newproposal.dto.response.ProposalAnalysisResponse;
import br.com.zup.jocivaldias.proposal.shared.exception.ApiErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ProposalAnalysisService {

    private ProposalAnalysis proposalAnalysisClient;

    public ProposalAnalysisService(ProposalAnalysis proposalAnalysisClient) {
        this.proposalAnalysisClient = proposalAnalysisClient;
    }

    public ProposalAnalysisResponse analyzesProposal(Proposal proposal){
        ProposalAnalysisRequest proposalAnalysisRequest = new ProposalAnalysisRequest(proposal.getDocumentNumber(),
                proposal.getName(),
                proposal.getId().toString());

        ProposalAnalysisResponse proposalAnalysisResponse = null;
        try {
             proposalAnalysisResponse = proposalAnalysisClient.requestAnalysis(proposalAnalysisRequest);
        } catch (FeignException.UnprocessableEntity unprocessableEntity){
                try {
                    proposalAnalysisResponse = new ObjectMapper().readValue(unprocessableEntity.contentUTF8(), ProposalAnalysisResponse.class);
                } catch (JsonProcessingException e) {
                    throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Service temporarily unavailable. Try again later");
                }
        } catch (FeignException feignException){
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Service temporarily unavailable. Try again later");
        }

        return proposalAnalysisResponse;
    }

}
