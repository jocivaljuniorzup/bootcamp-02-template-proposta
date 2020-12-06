package br.com.zup.jocivaldias.proposal.dto.response;

import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CreditCardResponse {

    @JsonProperty("id")
    private String creditCardNumber;

    @JsonProperty("emitidoEm")
    private LocalDateTime createdAt;

    @JsonProperty("titular")
    private String holder;

    public CreditCardResponse(String creditCardNumber, LocalDateTime createdAt, String holder) {
        this.creditCardNumber = creditCardNumber;
        this.createdAt = createdAt;
        this.holder = holder;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getHolder() {
        return holder;
    }
}
