package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.ProposalAnalysisRequest;
import br.com.zup.jocivaldias.proposal.dto.response.ProposalAnalysisResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${proposal.analysis.host}", name = "proposalAnalysis")
public interface ProposalAnalysis {

    @PostMapping(path = "/api/solicitacao", consumes = "application/json", produces = "application/json")
    ProposalAnalysisResponse requestAnalysis(@RequestBody ProposalAnalysisRequest body);

}
