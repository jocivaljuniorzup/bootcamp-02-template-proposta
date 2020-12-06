package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.LockCreditCardRequest;
import br.com.zup.jocivaldias.proposal.dto.request.TravelCreditCardRequest;
import br.com.zup.jocivaldias.proposal.dto.response.CreditCardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name="creditCardGenerator", url = "${credit.card.control.host}")
public interface CreditCardControl {

    @GetMapping(path = "/api/cartoes/{od}", consumes = "application/json", produces = "application/json")
    CreditCardResponse getCreditCard(@RequestParam(name="idProposta") String idProposal);

    @PostMapping(path = "/api/cartoes/{creditCardNumber}/bloqueios", consumes = "application/json", produces = "application/json")
    Map<String, String> informLockCreditCard(@PathVariable(name = "creditCardNumber") String creditCardNumber, @RequestBody LockCreditCardRequest lockRequest);

    @PostMapping(path = "/api/cartoes/{creditCardNumber}/avisos", consumes = "application/json", produces = "application/json")
    Map<String, String> informTravelCreditCard(@PathVariable(name = "creditCardNumber") String creditCardNumber, @RequestBody TravelCreditCardRequest request);

}
