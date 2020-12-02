package br.com.zup.jocivaldias.proposal.repository;

import br.com.zup.jocivaldias.proposal.entity.Biometric;
import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BiometricRepository extends JpaRepository<Biometric, UUID> {

}
