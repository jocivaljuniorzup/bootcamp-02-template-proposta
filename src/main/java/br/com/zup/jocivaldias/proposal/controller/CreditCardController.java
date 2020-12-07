package br.com.zup.jocivaldias.proposal.controller;

import br.com.zup.jocivaldias.proposal.dto.request.NewBiometricRequest;
import br.com.zup.jocivaldias.proposal.dto.request.NewTravelNoticeRequest;
import br.com.zup.jocivaldias.proposal.dto.request.NewWalletRequest;
import br.com.zup.jocivaldias.proposal.entity.*;
import br.com.zup.jocivaldias.proposal.entity.enums.DigitalWalletProvider;
import br.com.zup.jocivaldias.proposal.repository.*;
import br.com.zup.jocivaldias.proposal.service.CreditCardControlService;
import br.com.zup.jocivaldias.proposal.shared.exception.ApiErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/cards")
public class CreditCardController {

    private BiometricRepository biometricRepository;
    private CreditCardRepository creditCardRepository;
    private CardLockRepository cardLockRepository;
    private TravelNoticeRepository travelNoticeRepository;
    private CreditCardControlService creditCardControlService;
    private TransactionTemplate transactionTemplate;
    private DigitalWalletRepository digitalWalletRepository;

    public CreditCardController(BiometricRepository biometricRepository,
                                CreditCardRepository creditCardRepository,
                                CardLockRepository cardLockRepository,
                                TravelNoticeRepository travelNoticeRepository,
                                CreditCardControlService creditCardControlService,
                                TransactionTemplate transactionTemplate,
                                DigitalWalletRepository digitalWalletRepository) {
        this.biometricRepository = biometricRepository;
        this.creditCardRepository = creditCardRepository;
        this.cardLockRepository = cardLockRepository;
        this.travelNoticeRepository = travelNoticeRepository;
        this.creditCardControlService = creditCardControlService;
        this.transactionTemplate = transactionTemplate;
        this.digitalWalletRepository = digitalWalletRepository;
    }

    @PostMapping(path = "/{id}/biometrics/")
    @Transactional
    public ResponseEntity<?> createBiometrics(@PathVariable(name = "id") UUID id,
                                              @RequestBody @Valid NewBiometricRequest newBiometricRequest,
                                              UriComponentsBuilder uriComponentsBuilder){

        Biometric biometric = newBiometricRequest.toModel(id, creditCardRepository);
        biometricRepository.save(biometric);

        URI uri = uriComponentsBuilder.path("/cards/{cardId}/biometrics/{biometricId}")
                .buildAndExpand(id, biometric.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping(path = "/{id}/locks/")
    @Transactional
    public ResponseEntity<?> lockCard(@PathVariable(name = "id") UUID id,
                                      @RequestHeader(name="user-agent") @NotBlank String userAgent,
                                      HttpServletRequest request){

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(id);
        CreditCard creditCard = optionalCreditCard.orElseThrow(() -> {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "No credit card found for this ID");
        });

        if(creditCard.isBlocked(cardLockRepository)){
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Credit card already blocked.");
        }

        CardLock cardLock = new CardLock(request.getRemoteAddr(), userAgent, creditCard);
        cardLockRepository.save(cardLock);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}/travels/")
    public ResponseEntity<?> travelNotice(@PathVariable(name = "id") UUID id,
                                          @RequestBody @Valid NewTravelNoticeRequest newTravelNoticeRequest,
                                          @RequestHeader(name="user-agent") @NotBlank String userAgent,
                                          HttpServletRequest request){

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(id);
        CreditCard creditCard = optionalCreditCard.orElseThrow(() -> {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "No credit card found for this ID");
        });

        boolean success = creditCardControlService.informTripCreditCard(creditCard, newTravelNoticeRequest);
        if(success){
            TravelNotice travelNotice = newTravelNoticeRequest.toModel(creditCard, request.getRemoteAddr(), userAgent);
            transactionTemplate.execute(status -> {
                travelNoticeRepository.save(travelNotice);
                return true;
            });
        } else {
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error to process requisition");
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}/wallets/{walletProvider}")
    public ResponseEntity<?> registerWallet(@PathVariable(name = "id") UUID id,
                                            @PathVariable(name = "walletProvider") String walletProvider,
                                            @RequestBody @Valid NewWalletRequest newWalletRequest,
                                            UriComponentsBuilder uriComponentsBuilder){

        DigitalWalletProvider digitalWalletProvider = DigitalWalletProvider.toEnum(walletProvider);
        if(digitalWalletProvider == null){
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "Invalid Digital Wallet Provider");
        }

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(id);
        CreditCard creditCard = optionalCreditCard.orElseThrow(() -> {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "No credit card found for this ID");
        });

        Optional<DigitalWallet> alreadyAssociated = digitalWalletRepository.findByIdAndProvider(id, digitalWalletProvider);
        if(alreadyAssociated.isPresent()){
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Credit Card already associated with this provider");
        }

        boolean success = creditCardControlService.informDigitalWalletCreditCard(creditCard, newWalletRequest, digitalWalletProvider);
        if(success){
            DigitalWallet digitalWallet = newWalletRequest.toModel(digitalWalletProvider);
            transactionTemplate.execute(status -> {
                digitalWalletRepository.save(digitalWallet);
                return true;
            });

            URI uri = uriComponentsBuilder.path("/{id}/wallets/{wallet}/{id}")
                    .buildAndExpand(id, digitalWalletProvider.name().toLowerCase(), digitalWallet.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();
        } else {
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error to process requisition");
        }
    }

}
