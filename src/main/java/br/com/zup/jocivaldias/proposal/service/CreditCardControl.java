package br.com.zup.jocivaldias.proposal.service;

import br.com.zup.jocivaldias.proposal.dto.request.NewCreditCardRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="creditCardGenerator", url = "${credit.card.control.host}")
public interface CreditCardControl {

    @PostMapping(path = "/api/cartoes", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> generateCreditCard(@RequestBody NewCreditCardRequest newCreditCardRequest);

}
