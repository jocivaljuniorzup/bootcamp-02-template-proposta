package br.com.zup.jocivaldias.proposal.newproposal;

import br.com.zup.jocivaldias.proposal.newproposal.dto.request.NewProposalRequest;
import br.com.zup.jocivaldias.proposal.newproposal.dto.response.ProposalAnalysisResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "proposals")
public class NewProposalController {

    private final ProposalRepository proposalRepository;
    private final ProposalAnalysisService proposalAnalysisService;
    private final TransactionTemplate transactionTemplate;

    private final Logger logger = LoggerFactory.getLogger(NewProposalController.class);

    public NewProposalController(ProposalRepository proposalRepository,
                                 ProposalAnalysisService proposalAnalysisService,
                                 TransactionTemplate transactionTemplate) {
        this.proposalRepository = proposalRepository;
        this.proposalAnalysisService = proposalAnalysisService;
        this.transactionTemplate = transactionTemplate;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid NewProposalRequest newProposalRequest,
                                    UriComponentsBuilder uriComponentsBuilder) {

        Proposal proposal = newProposalRequest.toModel(proposalRepository);

        transactionTemplate.execute(status -> {
            proposalRepository.save(proposal);
            return proposal.getId();
        });

        ProposalAnalysisResponse proposalAnalysisResponse = proposalAnalysisService.analyzesProposal(proposal);
        if( proposalAnalysisResponse.getResultadoSolicitacao().equalsIgnoreCase("SEM_RESTRICAO")){
            proposal.setStatus(ProposalStatus.ELIGIBLE);
        } else {
            proposal.setStatus(ProposalStatus.NOT_ELIGIBLE);
        }

        transactionTemplate.execute(status -> {
            proposalRepository.save(proposal);
            return proposal.getId();
        });

        logger.info("New proposal created:" +
                "{id} = " + proposal.getId() + "," +
                "{status} = " + proposal.getStatus());

        URI uri = uriComponentsBuilder.path("/proposals/{id}").buildAndExpand(proposal.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
