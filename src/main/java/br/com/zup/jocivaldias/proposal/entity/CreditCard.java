package br.com.zup.jocivaldias.proposal.entity;

import br.com.zup.jocivaldias.proposal.repository.CreditCardLockRepository;
import br.com.zup.jocivaldias.proposal.shared.exception.ApiErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
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

    public UUID getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public boolean isBlocked(CreditCardLockRepository creditCardLockRepository) {
        return creditCardLockRepository.findByCreditCardId(id).isPresent();
    }
}
