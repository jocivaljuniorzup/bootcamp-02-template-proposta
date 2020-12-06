package br.com.zup.jocivaldias.proposal.repository;

import br.com.zup.jocivaldias.proposal.entity.CardLock;
import br.com.zup.jocivaldias.proposal.entity.enums.CreditCardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardLockRepository extends JpaRepository<CardLock, UUID> {

    Optional<CardLock> findByCreditCardId(UUID id);

    List<CardLock> findByCreditCardStatus(CreditCardStatus status);

}
