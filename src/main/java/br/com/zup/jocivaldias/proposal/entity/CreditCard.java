package br.com.zup.jocivaldias.proposal.entity;

import br.com.zup.jocivaldias.proposal.entity.enums.CreditCardStatus;
import br.com.zup.jocivaldias.proposal.repository.CreditCardLockRepository;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "binary(16)")
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String cardNumber;

    @NotNull
    @OneToOne(optional = false)
    private Proposal proposal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditCardStatus status;

    @Deprecated
    private CreditCard(){

    }

    public CreditCard(@NotBlank String cardNumber,
                      @NotNull Proposal proposal,
                      @NotNull CreditCardStatus creditCardStatus) {

        Assert.hasText(cardNumber, "Credit Card Number cant be blank");
        Assert.notNull(proposal, "Proposal cant be null");
        Assert.notNull(creditCardStatus, "Credit Card Status cant be null");

        this.cardNumber = cardNumber;
        this.proposal = proposal;
        this.status = creditCardStatus;
    }

    public UUID getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setCreditCardStatus(CreditCardStatus creditCardStatus) {
        this.status = creditCardStatus;
    }

    public boolean isBlocked(CreditCardLockRepository creditCardLockRepository) {
        return creditCardLockRepository.findByCreditCardId(id).isPresent();
    }


}
