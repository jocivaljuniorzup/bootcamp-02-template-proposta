package br.com.zup.jocivaldias.proposal.newproposal;

import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class CreditCard {

    @Id
    @NotBlank
    private String cardNumber;

    @NotNull
    @ManyToOne
    private Proposal proposal;

    @Deprecated
    private CreditCard(){

    }

    public CreditCard(@NotBlank String cardNumber,
                      @NotNull Proposal proposal) {

        Assert.hasText(cardNumber, "CardNumber cant be blank");
        Assert.notNull(proposal, "Proposal cant be null");

        this.cardNumber = cardNumber;
        this.proposal = proposal;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Proposal getProposal() {
        return proposal;
    }
}
