package br.com.zup.jocivaldias.proposal.newproposal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "proposals")
public class NewProposalController {

    @PersistenceContext
    private EntityManager entityManager;

    public NewProposalController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid NewProposalRequest newProposalRequest, UriComponentsBuilder uriComponentsBuilder){
        Proposal proposal = newProposalRequest.toModel();

        entityManager.persist(proposal);

        URI uri = uriComponentsBuilder.path("/proposals/{id}").buildAndExpand(proposal.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }


}
