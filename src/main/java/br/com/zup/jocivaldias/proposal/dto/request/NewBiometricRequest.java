package br.com.zup.jocivaldias.proposal.dto.request;

import br.com.zup.jocivaldias.proposal.entity.Biometric;
import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import br.com.zup.jocivaldias.proposal.repository.CreditCardRepository;
import br.com.zup.jocivaldias.proposal.shared.exception.ApiErrorException;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;

public class NewBiometricRequest {

    @NotBlank
    private String fingerprint;

    public NewBiometricRequest() {
    }

    public NewBiometricRequest(@NotBlank String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Biometric toModel(UUID id, CreditCardRepository creditCardRepository) {

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(id);

        CreditCard creditCard = optionalCreditCard.orElseThrow(() -> {
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "No credit card found for this ID");
        });

        return new Biometric(Base64.decode(fingerprint), creditCard);
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
