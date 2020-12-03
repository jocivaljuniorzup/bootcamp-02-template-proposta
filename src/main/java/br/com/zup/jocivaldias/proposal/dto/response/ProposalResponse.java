package br.com.zup.jocivaldias.proposal.dto.response;

import br.com.zup.jocivaldias.proposal.entity.Proposal;
import br.com.zup.jocivaldias.proposal.entity.enums.ProposalStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class ProposalResponse {

    private UUID id;
    private String documentNumber;
    private String email;
    private String name;
    private String address;
    private BigDecimal salary;
    private ProposalStatus status;
    private CreditCardResponse creditCard;

    public ProposalResponse(UUID id,
                            String documentNumber,
                            String email,
                            String name,
                            String address,
                            BigDecimal salary,
                            ProposalStatus status,
                            CreditCardResponse creditCard) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.email = email;
        this.name = name;
        this.address = address;
        this.salary = salary;
        this.status = status;
        this.creditCard = creditCard;
    }

    public static ProposalResponse fromModel(Proposal proposal) {
        CreditCardResponse creditCardResponse = null;
        if(proposal.getCreditCard() != null){
             creditCardResponse = CreditCardResponse.toModel(proposal.getCreditCard());
        }

        return new ProposalResponse(
                proposal.getId(),
                proposal.getDocumentNumber(),
                proposal.getEmail(),
                proposal.getName(),
                proposal.getAddress(),
                proposal.getSalary(),
                proposal.getStatus(),
                creditCardResponse
        );
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

    public ProposalStatus getStatus() {
        return status;
    }

    public CreditCardResponse getCreditCard() {
        return creditCard;
    }
}
