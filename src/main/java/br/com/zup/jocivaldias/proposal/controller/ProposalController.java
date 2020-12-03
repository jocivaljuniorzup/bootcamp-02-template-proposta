package br.com.zup.jocivaldias.proposal.controller;

import br.com.zup.jocivaldias.proposal.dto.request.NewProposalRequest;
import br.com.zup.jocivaldias.proposal.dto.response.ProposalAnalysisResponse;
import br.com.zup.jocivaldias.proposal.dto.response.ProposalResponse;
import br.com.zup.jocivaldias.proposal.entity.Proposal;
import br.com.zup.jocivaldias.proposal.service.ProposalAnalysisService;
import br.com.zup.jocivaldias.proposal.repository.ProposalRepository;
import br.com.zup.jocivaldias.proposal.entity.enums.ProposalStatus;
import br.com.zup.jocivaldias.proposal.shared.exception.ApiErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "proposals")
public class ProposalController {

    private final ProposalRepository proposalRepository;
    private final ProposalAnalysisService proposalAnalysisService;
    private final TransactionTemplate transactionTemplate;

    private final Logger logger = LoggerFactory.getLogger(ProposalController.class);

    public ProposalController(ProposalRepository proposalRepository,
                              ProposalAnalysisService proposalAnalysisService,
                              TransactionTemplate transactionTemplate) {
        this.proposalRepository = proposalRepository;
        this.proposalAnalysisService = proposalAnalysisService;
        this.transactionTemplate = transactionTemplate;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid NewProposalRequest newProposalRequest,
                                    UriComponentsBuilder uriComponentsBuilder) {

        logger.error("exemplo de log só pra ver");
        Proposal proposal = newProposalRequest.toModel(proposalRepository);

        transactionTemplate.execute(status -> {
            proposalRepository.save(proposal);
            return proposal.getId();
        });



        URI uri = uriComponentsBuilder.path("/proposals/{id}").buildAndExpand(proposal.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProposalResponse> find(@PathVariable(name="id") UUID id){
        Optional<Proposal> optionalProposal = proposalRepository.findById(id);

        Proposal proposal = optionalProposal.orElseThrow(() -> {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "Proposal not found.");
        });

        return ResponseEntity.ok(ProposalResponse.fromModel(proposal));
    }

}
