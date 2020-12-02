package br.com.zup.jocivaldias.proposal.entity;

import org.hibernate.annotations.CreationTimestamp;

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

    //TODO: Verificar se é para salvar as features da biometria no banco de dados ou a imagem da biometria no filesystem
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

    public Biometric(byte[] fingerprint, @NotNull CreditCard creditCard) {
        this.fingerprint = fingerprint;
        this.creditCard = creditCard;
    }

    public UUID getId() {
        return id;
    }
}
