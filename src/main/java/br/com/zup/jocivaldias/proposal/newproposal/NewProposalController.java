package br.com.zup.jocivaldias.proposal.newproposal;

import br.com.zup.jocivaldias.proposal.shared.exception.ApiErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "proposals")
public class NewProposalController {

    @PersistenceContext
    private EntityManager entityManager;

    private final Logger logger = LoggerFactory.getLogger(NewProposalController.class);

    public NewProposalController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid NewProposalRequest newProposalRequest, UriComponentsBuilder uriComponentsBuilder){
        Query query = entityManager.createQuery("select 1 from Proposal where document_number = :value");
        query.setParameter("value", newProposalRequest.getDocumentNumber());
        List<?> resultList = query.getResultList();

        if(!resultList.isEmpty())
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "There is already a proposal associated with this CPF / CNPJ");

        Proposal proposal = newProposalRequest.toModel();
        entityManager.persist(proposal);
        logger.info("New proposal created: {id} = " + proposal.getId());

        URI uri = uriComponentsBuilder.path("/proposals/{id}").buildAndExpand(proposal.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
