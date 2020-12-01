package br.com.zup.jocivaldias.proposal.dto.response;

import br.com.zup.jocivaldias.proposal.entity.CreditCard;

public class CreditCardResponse {

    private String creditCardNumber;

    public CreditCardResponse(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public static CreditCardResponse toModel(CreditCard creditCard) {
        return new CreditCardResponse(creditCard.getCardNumber());
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }


}
