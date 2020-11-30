package br.com.zup.jocivaldias.proposal.newproposal;

import br.com.zup.jocivaldias.proposal.shared.CpfCnpj;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class NewProposalRequest {

    @NotBlank
    @CpfCnpj
    private String documentNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotNull
    @PositiveOrZero
    private BigDecimal salary;

    public NewProposalRequest(@NotBlank String documentNumber,
                              @NotBlank String email,
                              @NotBlank String name,
                              @NotBlank String address,
                              @NotNull @PositiveOrZero BigDecimal salary) {
        this.documentNumber = documentNumber;
        this.email = email;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }


    public Proposal toModel() {
        return new Proposal(documentNumber, email, name, address, salary);
    }
}
