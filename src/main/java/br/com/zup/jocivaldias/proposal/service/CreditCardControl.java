package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.InformDigitalWalletRequest;
import br.com.zup.jocivaldias.proposal.dto.request.InformLockCreditCardRequest;
import br.com.zup.jocivaldias.proposal.dto.request.InformTravelCreditCardRequest;
import br.com.zup.jocivaldias.proposal.dto.response.CreditCardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name="creditCardGenerator", url = "${credit.card.control.host}")
public interface CreditCardControl {

    @GetMapping(path = "/api/cartoes/{od}", consumes = "application/json", produces = "application/json")
    CreditCardResponse getCreditCard(@RequestParam(name="idProposta") String idProposal);

    @PostMapping(path = "/api/cartoes/{creditCardNumber}/bloqueios", consumes = "application/json", produces = "application/json")
    Map<String, String> informLockCreditCard(@PathVariable(name = "creditCardNumber") String creditCardNumber, @RequestBody InformLockCreditCardRequest lockRequest);

    @PostMapping(path = "/api/cartoes/{creditCardNumber}/avisos", consumes = "application/json", produces = "application/json")
    Map<String, String> informTravelCreditCard(@PathVariable(name = "creditCardNumber") String creditCardNumber, @RequestBody InformTravelCreditCardRequest request);

    @PostMapping(path = "/api/cartoes/{creditCardNumber}/carteiras", consumes = "application/json", produces = "application/json")
    Map<String, String> informDigitalWallet(@PathVariable(name = "creditCardNumber") String creditCardNumber, @RequestBody InformDigitalWalletRequest request);

}
