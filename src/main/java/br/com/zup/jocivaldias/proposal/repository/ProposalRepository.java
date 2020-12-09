package br.com.zup.jocivaldias.proposal.repository;

import br.com.zup.jocivaldias.proposal.entity.Proposal;
import br.com.zup.jocivaldias.proposal.entity.enums.ProposalStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, UUID> {

    List<Proposal> findAllByDocumentNumber(String documentNumber);

    List<Proposal> findAllByStatus(ProposalStatus status, Pageable pageable);

}
