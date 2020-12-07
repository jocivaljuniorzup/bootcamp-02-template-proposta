package br.com.zup.jocivaldias.proposal.entity;

import br.com.zup.jocivaldias.proposal.entity.enums.DigitalWalletProvider;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class DigitalWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DigitalWalletProvider provider;

    @ManyToOne(optional = false)
    private CreditCard creditCard;

    @Deprecated
    private DigitalWallet() {
    }

    public DigitalWallet(@NotBlank @Email String email,
                         @NotNull DigitalWalletProvider provider,
                         @NotNull CreditCard creditCard) {

        Assert.hasText(email, "Email cant be blank");
        Assert.notNull(provider, "Digital Wallet Provider cant be null");
        Assert.notNull(creditCard, "CreditCard cant be null");

        this.email = email;
        this.provider = provider;
        this.creditCard = creditCard;
    }

    public UUID getId() {
        return id;
    }
}
