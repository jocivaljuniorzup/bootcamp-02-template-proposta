package br.com.zup.jocivaldias.proposal.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class CreditCardLock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "binary(16)")
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime blockedAt;

    @NotBlank
    @Column(nullable = false)
    private String requestIp;

    @NotBlank
    @Column(nullable = false)
    private String requestUserAgent;

    @NotNull
    @OneToOne(optional = false)
    private CreditCard creditCard;

    @Deprecated
    private CreditCardLock() {
    }

    public CreditCardLock(@NotBlank String requestIp,
                          @NotBlank String requestUserAgent,
                          @NotNull CreditCard creditCard) {

        Assert.hasText(requestIp, "User ip cant be blank");
        Assert.hasText(requestUserAgent, "Request user agent cant be blank");
        Assert.notNull(creditCard, "CreditCard cant be null");

        this.requestIp = requestIp;
        this.requestUserAgent = requestUserAgent;
        this.creditCard = creditCard;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }
}
