package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.NewCreditCardRequest;
import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import br.com.zup.jocivaldias.proposal.entity.Proposal;
import br.com.zup.jocivaldias.proposal.entity.enums.ProposalStatus;
import br.com.zup.jocivaldias.proposal.repository.CreditCardRepository;
import br.com.zup.jocivaldias.proposal.repository.ProposalRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class CreditCardControlService {

    private CreditCardControl creditCardControl;
    private ProposalRepository proposalRepository;
    private CreditCardRepository creditCardRepository;
    private final TransactionTemplate transactionTemplate;

    private Logger logger = LoggerFactory.getLogger(CreditCardControlService.class);

    public CreditCardControlService(CreditCardControl creditCardControl,
                                    ProposalRepository proposalRepository,
                                    CreditCardRepository creditCardRepository,
                                    TransactionTemplate transactionTemplate) {
        this.creditCardControl = creditCardControl;
        this.proposalRepository = proposalRepository;
        this.creditCardRepository = creditCardRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    private void generateCreditCard(){
        List<Proposal> proposalByStatus = proposalRepository.findByStatus(ProposalStatus.ELIGIBLE);

        for (Proposal proposal : proposalByStatus) {
            NewCreditCardRequest newCreditCardRequest = new NewCreditCardRequest(proposal.getDocumentNumber(),
                    proposal.getName(),
                    proposal.getId().toString());

            try {
                ResponseEntity<?> responseEntity = creditCardControl.generateCreditCard(newCreditCardRequest);

                if (responseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                    String uri = responseEntity.getHeaders().get("Location").get(0);
                    String cardNumber = uri.substring(uri.lastIndexOf("/") + 1);
                    proposal.setStatus(ProposalStatus.CREDIT_CARD_ISSUED);

                    transactionTemplate.execute(status -> {
                        creditCardRepository.save(new CreditCard(cardNumber, proposal));
                        proposalRepository.save(proposal);
                        return true;
                    });

                } else {
                    logger.error("Invalid return in Credit Card Generator API - Status code: {}, Body: {}",
                            responseEntity.getStatusCodeValue(),
                            responseEntity.getBody());
                }

            } catch (FeignException exception){
                logger.error("Error in Credit Card Generator API - Status code: {}, Body: {}, Message: {}",
                        exception.status(),
                        exception.contentUTF8(),
                        exception.getMessage());
            }
        }
    }

}
