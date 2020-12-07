package br.com.zup.jocivaldias.proposal.entity;

import br.com.zup.jocivaldias.proposal.entity.enums.DigitalWalletProvider;

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

    @Deprecated
    private DigitalWallet() {
    }

    public DigitalWallet(@NotBlank @Email String email, @NotNull DigitalWalletProvider provider) {
        this.email = email;
        this.provider = provider;
    }

    public UUID getId() {
        return id;
    }
}
