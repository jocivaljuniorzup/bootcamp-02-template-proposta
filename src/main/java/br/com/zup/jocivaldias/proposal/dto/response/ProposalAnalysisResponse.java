package br.com.zup.jocivaldias.proposal.dto.response;

public class ProposalAnalysisResponse {

    private String documento;
    private String nome;
    private String idProposta;
    private String resultadoSolicitacao;

    public ProposalAnalysisResponse() {
    }

    public ProposalAnalysisResponse(String documento, String nome, String idProposta, String resultadoSolicitacao) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
        this.resultadoSolicitacao = resultadoSolicitacao;
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

    public String getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    @Override
    public String toString() {
        return "ProposalAnalysisResponse{" +
                "documento='" + documento + '\'' +
                ", nome='" + nome + '\'' +
                ", idProposta='" + idProposta + '\'' +
                ", resultadoSolicitacao='" + resultadoSolicitacao + '\'' +
                '}';
    }
}
