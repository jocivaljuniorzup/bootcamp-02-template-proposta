package br.com.zup.jocivaldias.proposal.controller;

import br.com.zup.jocivaldias.proposal.dto.request.NewBiometricRequest;
import br.com.zup.jocivaldias.proposal.entity.Biometric;
import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import br.com.zup.jocivaldias.proposal.entity.CreditCardLock;
import br.com.zup.jocivaldias.proposal.repository.BiometricRepository;
import br.com.zup.jocivaldias.proposal.repository.CreditCardLockRepository;
import br.com.zup.jocivaldias.proposal.repository.CreditCardRepository;
import br.com.zup.jocivaldias.proposal.service.CreditCardControlService;
import br.com.zup.jocivaldias.proposal.shared.exception.ApiErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private CreditCardLockRepository creditCardLockRepository;

    public CreditCardController(BiometricRepository biometricRepository,
                                CreditCardRepository creditCardRepository,
                                CreditCardLockRepository creditCardLockRepository) {
        this.biometricRepository = biometricRepository;
        this.creditCardRepository = creditCardRepository;
        this.creditCardLockRepository = creditCardLockRepository;
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

        if(creditCard.isBlocked(creditCardLockRepository)){
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Credit card already blocked.");
        }

        CreditCardLock creditCardLock = new CreditCardLock(request.getRemoteAddr(), userAgent, creditCard);
        creditCardLockRepository.save(creditCardLock);

        return ResponseEntity.ok().build();
    }

}
