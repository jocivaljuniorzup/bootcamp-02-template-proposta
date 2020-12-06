package br.com.zup.jocivaldias.proposal.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class TravelCreditCardRequest {

    @JsonProperty("destino")
    private String destination;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @JsonProperty("validoAte")
    private LocalDate validUntil;

    public TravelCreditCardRequest(String destination, LocalDate validUntil) {
        this.destination = destination;
        this.validUntil = validUntil;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    @Override
    public String toString() {
        return "TravelCreditCardRequest{" +
                "destination='" + destination + '\'' +
                ", validUntil=" + validUntil +
                '}';
    }
}
