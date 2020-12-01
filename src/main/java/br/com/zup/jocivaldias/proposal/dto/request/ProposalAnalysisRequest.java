package br.com.zup.jocivaldias.proposal.dto.request;

public class ProposalAnalysisRequest {
    private String documento;
    private String nome;
    private String idProposta;

    public ProposalAnalysisRequest() {
    }

    public ProposalAnalysisRequest(String documento, String nome, String idProposta) {
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