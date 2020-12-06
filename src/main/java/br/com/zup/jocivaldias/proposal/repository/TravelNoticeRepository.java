package br.com.zup.jocivaldias.proposal.repository;

import br.com.zup.jocivaldias.proposal.entity.TravelNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TravelNoticeRepository extends JpaRepository<TravelNotice, UUID> {
}
