package br.com.zup.jocivaldias.proposal.newproposal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, UUID> {

    public List<Proposal> findByDocumentNumber(String documentNumber);

    public List<Proposal> findByStatus(ProposalStatus status);

}
