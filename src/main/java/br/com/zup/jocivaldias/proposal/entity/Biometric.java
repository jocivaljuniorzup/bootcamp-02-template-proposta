package br.com.zup.jocivaldias.proposal.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Biometric {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "binary(16)")
    private UUID id;

    @Lob
    @NotNull
    @Column(nullable = false)
    private byte[] fingerprint;

    @NotNull
    @ManyToOne
    private CreditCard creditCard;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Deprecated
    private Biometric() {
    }

    public Biometric(@NotNull byte[] fingerprint, @NotNull CreditCard creditCard) {

        Assert.notNull(fingerprint, "Fingerprint cant be null");
        Assert.notNull(creditCard, "CreditCard cant be null");

        this.fingerprint = fingerprint;
        this.creditCard = creditCard;
    }

    public UUID getId() {
        return id;
    }
}
