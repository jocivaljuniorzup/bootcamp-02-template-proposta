package br.com.zup.jocivaldias.proposal.newproposal.dto.request;

public class NewCreditCardRequest {

    private String documento;
    private String nome;
    private String idProposta;

    public NewCreditCardRequest(String documento, String nome, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
