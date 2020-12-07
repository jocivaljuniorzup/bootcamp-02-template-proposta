package br.com.zup.jocivaldias.proposal.dto.request;

import br.com.zup.jocivaldias.proposal.entity.enums.DigitalWalletProvider;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InformDigitalWalletRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("carteira")
    private DigitalWalletProvider walletProvider;

    public InformDigitalWalletRequest(String email, DigitalWalletProvider walletProvider) {
        this.email = email;
        this.walletProvider = walletProvider;
    }

}
