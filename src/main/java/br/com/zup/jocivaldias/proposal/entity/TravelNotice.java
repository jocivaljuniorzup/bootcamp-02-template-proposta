package br.com.zup.jocivaldias.proposal.entity;

import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class TravelNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "binary(16)")
    private UUID id;

    @NotNull
    @ManyToOne(optional = false)
    private CreditCard creditCard;

    @NotBlank
    @Column(nullable = false)
    private String destination;

    @NotNull
    @Column(nullable = false)
    @FutureOrPresent
    private LocalDate endDate;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @NotBlank
    @Column(nullable = false)
    private String requestIp;

    @NotBlank
    @Column(nullable = false)
    private String requestUserAgent;

    @Deprecated
    private TravelNotice() {
    }

    public TravelNotice(@NotNull CreditCard creditCard,
                        @NotBlank String destination,
                        @NotNull LocalDate endDate,
                        @NotBlank String requestIp,
                        @NotBlank String requestUserAgent) {

        Assert.notNull(creditCard, "CreditCard cant be null");
        Assert.hasText(destination, "Destination cant be blank");
        Assert.notNull(endDate, "End date of trip cant be null");
        Assert.hasText(requestIp, "Request IP cant be blank");
        Assert.hasText(requestUserAgent, "Request User Agent cant be blank");

        this.creditCard = creditCard;
        this.destination = destination;
        this.endDate = endDate;
        this.requestIp = requestIp;
        this.requestUserAgent = requestUserAgent;
    }
}
