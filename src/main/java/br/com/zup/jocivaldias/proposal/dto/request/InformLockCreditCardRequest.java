package br.com.zup.jocivaldias.proposal.dto.request;

public class InformLockCreditCardRequest {

    private String sistemaResponsavel;

    public InformLockCreditCardRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
