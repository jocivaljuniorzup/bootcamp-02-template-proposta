package br.com.zup.jocivaldias.proposal.repository;

import br.com.zup.jocivaldias.proposal.entity.DigitalWallet;
import br.com.zup.jocivaldias.proposal.entity.enums.DigitalWalletProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DigitalWalletRepository extends JpaRepository<DigitalWallet, UUID> {

    Optional<DigitalWallet> findByCreditCardIdAndProvider(UUID creditCardId, DigitalWalletProvider provider);

}
