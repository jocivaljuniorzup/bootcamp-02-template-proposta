package br.com.zup.jocivaldias.proposal.controller;

import br.com.zup.jocivaldias.proposal.dto.request.NewBiometricRequest;
import br.com.zup.jocivaldias.proposal.entity.Biometric;
import br.com.zup.jocivaldias.proposal.repository.BiometricRepository;
import br.com.zup.jocivaldias.proposal.repository.CreditCardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/cards")
public class CreditCardController {

    private BiometricRepository biometricRepository;

    private CreditCardRepository creditCardRepository;

    public CreditCardController(BiometricRepository biometricRepository,
                                CreditCardRepository creditCardRepository) {
        this.biometricRepository = biometricRepository;
        this.creditCardRepository = creditCardRepository;
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


}
