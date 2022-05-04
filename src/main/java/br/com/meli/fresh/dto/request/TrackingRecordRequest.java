package br.com.meli.fresh.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrackingRecordRequest {
    @Size(min = 5)
    @NotBlank(message = "location must not be blank.")
    private String location;

    @Size(min = 5)
    @NotBlank(message = "detailMessage must not be blank.")
    private String detailMessage;

    @Size(min = 5)
    @NotBlank(message = "orderStatus must not be blank.")
    private String orderStatus;

    @NotBlank(message = "orderId must not be blank.")
    private String orderId;
}
