package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.LockCreditCardRequest;
import br.com.zup.jocivaldias.proposal.dto.request.NewTravelNoticeRequest;
import br.com.zup.jocivaldias.proposal.dto.request.TravelCreditCardRequest;
import br.com.zup.jocivaldias.proposal.dto.response.CreditCardResponse;
import br.com.zup.jocivaldias.proposal.entity.CardLock;
import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import br.com.zup.jocivaldias.proposal.entity.Proposal;
import br.com.zup.jocivaldias.proposal.entity.enums.CreditCardStatus;
import br.com.zup.jocivaldias.proposal.entity.enums.ProposalStatus;
import br.com.zup.jocivaldias.proposal.repository.CardLockRepository;
import br.com.zup.jocivaldias.proposal.repository.CreditCardRepository;
import br.com.zup.jocivaldias.proposal.repository.ProposalRepository;
import br.com.zup.jocivaldias.proposal.shared.exception.ApiErrorException;
import feign.FeignException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private CardLockRepository cardLockRepository;
    private final TransactionTemplate transactionTemplate;

    private Logger logger = LoggerFactory.getLogger(CreditCardControlService.class);

    public CreditCardControlService(CreditCardControl creditCardControl,
                                    ProposalRepository proposalRepository,
                                    CreditCardRepository creditCardRepository,
                                    CardLockRepository cardLockRepository,
                                    TransactionTemplate transactionTemplate) {
        this.creditCardControl = creditCardControl;
        this.proposalRepository = proposalRepository;
        this.creditCardRepository = creditCardRepository;
        this.cardLockRepository = cardLockRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    private void getCreditCard(){
        List<Proposal> proposalByStatus = proposalRepository.findByStatus(ProposalStatus.ELIGIBLE);

        for (Proposal proposal : proposalByStatus) {
            try {
                CreditCardResponse creditCardResponse = creditCardControl.getCreditCard(proposal.getId().toString());
                proposal.setStatus(ProposalStatus.CREDIT_CARD_ISSUED);

                CreditCard creditCard = new CreditCard(creditCardResponse.getCreditCardNumber(),
                        creditCardResponse.getHolder(),
                        proposal,
                        creditCardResponse.getCreatedAt(),
                        CreditCardStatus.UNLOCKED);

                transactionTemplate.execute(status -> {
                    creditCardRepository.save(creditCard);
                    proposalRepository.save(proposal);
                    return true;
                });

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

        List<CardLock> unlockedCardsWithCardLock = cardLockRepository.findByCreditCardStatus(CreditCardStatus.UNLOCKED);

        for(CardLock cardLock : unlockedCardsWithCardLock){
            try {
                CreditCard creditCard = cardLock.getCreditCard();
                Map<String, String> response = creditCardControl.informLockCreditCard(creditCard.getCardNumber(), request);
                if (response.get("resultado").equalsIgnoreCase("BLOQUEADO")) {
                    creditCard.setCreditCardStatus(CreditCardStatus.LOCKED);
                    transactionTemplate.execute(status -> {
                        creditCardRepository.save(creditCard);
                        return true;
                    });
                } else {
                    logger.error("Invalid return in Inform Credit Card CardLock API - Body: {}", response);
                }
            } catch (FeignException feignException){
                logger.error("Error in Inform Credit Card CardLock API - Status code: {}, Body: {}, Message: {}",
                        feignException.status(),
                        feignException.contentUTF8(),
                        feignException.getMessage());
            }
        }
    }

    public boolean informTripCreditCard(CreditCard creditCard, NewTravelNoticeRequest newTravelNoticeRequest){
        TravelCreditCardRequest travelCreditCardRequest = new TravelCreditCardRequest(newTravelNoticeRequest.getDestination(),
                newTravelNoticeRequest.getEndDate());

        System.out.println(travelCreditCardRequest);

        try {
            Map<String, String> response = creditCardControl.informTravelCreditCard(creditCard.getCardNumber(), travelCreditCardRequest);
            System.out.println("Pica preta\n\n: " + response);
            if (response.get("resultado").equalsIgnoreCase("CRIADO")) {
                return true;
            } else {
                logger.error("Invalid return in Inform Credit Card Travel API - Body: {}", response);
                return false;
            }
        } catch (FeignException feignException){
            logger.error("Error in Inform Credit Card Travel API - Status code: {}, Body: {}, Message: {}",
                    feignException.status(),
                    feignException.contentUTF8(),
                    feignException.getMessage());
            return false;
        }
    }


}
