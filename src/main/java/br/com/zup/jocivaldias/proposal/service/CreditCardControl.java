package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.LockCreditCardRequest;
import br.com.zup.jocivaldias.proposal.dto.request.NewCreditCardRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name="creditCardGenerator", url = "${credit.card.control.host}")
public interface CreditCardControl {

    @PostMapping(path = "/api/cartoes", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> generateCreditCard(@RequestBody NewCreditCardRequest newCreditCardRequest);

    @PostMapping(path = "/api/cartoes/{creditCardNumber}/bloqueios", consumes = "application/json", produces = "application/json")
    Map<String, String> informLockForCreditCard(@PathVariable(name = "creditCardNumber") String creditCardNumber, @RequestBody LockCreditCardRequest lockRequest);

}
