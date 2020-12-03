package br.com.zup.jocivaldias.proposal.dto.request;

public class LockCreditCardRequest {

    private String sistemaResponsavel;

    public LockCreditCardRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
