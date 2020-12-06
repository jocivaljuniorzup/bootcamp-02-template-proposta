package br.com.zup.jocivaldias.proposal.entity;

import br.com.zup.jocivaldias.proposal.entity.enums.CreditCardStatus;
import br.com.zup.jocivaldias.proposal.repository.CardLockRepository;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    @NotBlank
    @Column(nullable = false)
    private String holder;

    @NotNull
    @OneToOne(optional = false)
    private Proposal proposal;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditCardStatus status;

    @Deprecated
    private CreditCard(){

    }

    public CreditCard(@NotBlank String cardNumber,
                      @NotBlank String holder,
                      @NotNull Proposal proposal,
                      @NotNull LocalDateTime createdAt,
                      @NotNull CreditCardStatus status) {

        Assert.hasText(cardNumber, "Credit Card Number cant be blank");
        Assert.hasText(holder, "Credit Card Holder cant be blank");
        Assert.notNull(proposal, "Proposal cant be null");
        Assert.notNull(createdAt, "Credit Card creation date cant be null");
        Assert.notNull(status, "Credit Card Status cant be null");

        this.cardNumber = cardNumber;
        this.holder = holder;
        this.proposal = proposal;
        this.createdAt = createdAt;
        this.status = status;
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

    public boolean isBlocked(CardLockRepository cardLockRepository) {
        return cardLockRepository.findByCreditCardId(id).isPresent();
    }


}
