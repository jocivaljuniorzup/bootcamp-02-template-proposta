package br.com.zup.jocivaldias.proposal.dto.request;

import br.com.zup.jocivaldias.proposal.entity.DigitalWallet;
import br.com.zup.jocivaldias.proposal.entity.enums.DigitalWalletProvider;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class NewWalletRequest {

    @NotBlank
    @Email
    private String email;

    public NewWalletRequest(@NotBlank @Email String email) {
        this.email = email;
    }

    public NewWalletRequest() {
    }

    public DigitalWallet toModel(DigitalWalletProvider digitalWalletProvider){
        return new DigitalWallet(email, digitalWalletProvider);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
