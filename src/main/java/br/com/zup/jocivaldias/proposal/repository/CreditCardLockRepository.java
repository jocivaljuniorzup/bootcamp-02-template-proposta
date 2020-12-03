package br.com.zup.jocivaldias.proposal.repository;

import br.com.zup.jocivaldias.proposal.entity.CreditCardLock;
import br.com.zup.jocivaldias.proposal.entity.enums.CreditCardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditCardLockRepository extends JpaRepository<CreditCardLock, UUID> {

    Optional<CreditCardLock> findByCreditCardId(UUID id);

    List<CreditCardLock> findByCreditCardStatus(CreditCardStatus status);

}
