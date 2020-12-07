package br.com.zup.jocivaldias.proposal.dto.request;

import br.com.zup.jocivaldias.proposal.entity.Biometric;
import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import org.bouncycastle.util.encoders.Base64;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class NewBiometricRequest {

    @NotBlank
    private String fingerprint;

    public NewBiometricRequest() {
    }

    public NewBiometricRequest(@NotBlank String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Biometric toModel(UUID id, CreditCard creditCard) {

        return new Biometric(Base64.decode(fingerprint), creditCard);
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
