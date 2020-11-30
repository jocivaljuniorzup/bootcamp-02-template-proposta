package br.com.zup.jocivaldias.proposal.newproposal;

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
        } catch (FeignException.FeignClientException feignClientException){
            if( feignClientException.status() == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
                try {
                    proposalAnalysisResponse = new ObjectMapper().readValue(feignClientException.contentUTF8(), ProposalAnalysisResponse.class);
                } catch (JsonProcessingException e) {
                    throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Service temporarily unavailable. Try again later");
                }
            }
            else
                throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Service temporarily unavailable. Try again later");
        } catch (FeignException.FeignServerException feignClientException){
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Service temporarily unavailable. Try again later");
        }

        return proposalAnalysisResponse;
    }

}
