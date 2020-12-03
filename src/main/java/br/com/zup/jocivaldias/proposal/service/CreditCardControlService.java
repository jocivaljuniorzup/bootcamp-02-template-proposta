package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.LockCreditCardRequest;
import br.com.zup.jocivaldias.proposal.dto.request.NewCreditCardRequest;
import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import br.com.zup.jocivaldias.proposal.entity.CreditCardLock;
import br.com.zup.jocivaldias.proposal.entity.Proposal;
import br.com.zup.jocivaldias.proposal.entity.enums.CreditCardStatus;
import br.com.zup.jocivaldias.proposal.entity.enums.ProposalStatus;
import br.com.zup.jocivaldias.proposal.repository.CreditCardLockRepository;
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
import java.util.Map;

@Component
public class CreditCardControlService {

    private CreditCardControl creditCardControl;
    private ProposalRepository proposalRepository;
    private CreditCardRepository creditCardRepository;
    private CreditCardLockRepository creditCardLockRepository;
    private final TransactionTemplate transactionTemplate;

    private Logger logger = LoggerFactory.getLogger(CreditCardControlService.class);

    public CreditCardControlService(CreditCardControl creditCardControl,
                                    ProposalRepository proposalRepository,
                                    CreditCardRepository creditCardRepository,
                                    CreditCardLockRepository creditCardLockRepository,
                                    TransactionTemplate transactionTemplate) {
        this.creditCardControl = creditCardControl;
        this.proposalRepository = proposalRepository;
        this.creditCardRepository = creditCardRepository;
        this.creditCardLockRepository = creditCardLockRepository;
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
                        creditCardRepository.save(new CreditCard(cardNumber, proposal, CreditCardStatus.UNLOCKED));
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

    @Scheduled(fixedDelay = 4000)
    public void informLockCreditCard(){
        LockCreditCardRequest request = new LockCreditCardRequest("proposals");

        List<CreditCardLock> unlockedCardsWithLock = creditCardLockRepository.findByCreditCardStatus(CreditCardStatus.UNLOCKED);

        for(CreditCardLock creditCardLock : unlockedCardsWithLock){
            try {
                CreditCard creditCard = creditCardLock.getCreditCard();
                Map<String, String> response = creditCardControl.informLockForCreditCard(creditCard.getCardNumber(), request);
                if (response.get("resultado").equalsIgnoreCase("BLOQUEADO")) {
                    creditCard.setCreditCardStatus(CreditCardStatus.LOCKED);
                    transactionTemplate.execute(status -> {
                        creditCardRepository.save(creditCard);
                        return true;
                    });
                } else {
                    logger.error("Invalid return in Inform Credit Card Lock API - Body: {}", response);
                }
            } catch (FeignException feignException){
                logger.error("Error in Inform Credit Card Lock API - Status code: {}, Body: {}, Message: {}",
                        feignException.status(),
                        feignException.contentUTF8(),
                        feignException.getMessage());
            }
        }
    }

}
