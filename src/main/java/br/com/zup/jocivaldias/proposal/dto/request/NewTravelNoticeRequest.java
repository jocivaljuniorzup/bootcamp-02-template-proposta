package br.com.zup.jocivaldias.proposal.dto.request;

import br.com.zup.jocivaldias.proposal.entity.CreditCard;
import br.com.zup.jocivaldias.proposal.entity.TravelNotice;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class NewTravelNoticeRequest {

    @NotBlank
    private String destination;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

    public NewTravelNoticeRequest(@NotBlank String destination,
                                  @NotNull LocalDate endDate) {
        this.destination = destination;
        this.endDate = endDate;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public TravelNotice toModel(CreditCard creditCard, String requestIp, String userAgent) {
        return new TravelNotice(creditCard, destination, endDate, requestIp, userAgent);
    }

    @Override
    public String toString() {
        return "NewTravelNoticeRequest{" +
                "destination='" + destination + '\'' +
                ", endDate=" + endDate +
                '}';
    }
}
