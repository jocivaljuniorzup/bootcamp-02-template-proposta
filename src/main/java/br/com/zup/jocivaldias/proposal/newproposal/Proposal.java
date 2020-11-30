package br.com.zup.jocivaldias.proposal.newproposal;

import br.com.zup.jocivaldias.proposal.shared.CpfCnpj;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "binary(16)")
    private UUID id;

    @NotBlank
    @CpfCnpj
    @Column(nullable = false)
    private String documentNumber;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotNull
    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal salary;

    private Proposal(){

    }

    public Proposal(@NotBlank String documentNumber,
                    @NotBlank @Email String email,
                    @NotBlank String name,
                    @NotBlank String address,
                    @NotNull @PositiveOrZero BigDecimal salary) {

        Assert.hasText(documentNumber, "Invalid documentNumber");
        Assert.hasText(email, "Invalid email");
        Assert.hasText(name, "Invalid name");
        Assert.hasText(address, "Invalid address");
        Assert.notNull(salary, "Invalid salary");
        Assert.isTrue(salary.compareTo(BigDecimal.ZERO) >= 0, "Salary cant be negative");

        this.documentNumber = documentNumber;
        this.email = email;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    public UUID getId() {
        return id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getSalary() {
        return salary;
    }
}
