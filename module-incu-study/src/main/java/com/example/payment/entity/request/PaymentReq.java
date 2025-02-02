package com.example.payment.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReq {
    private Long partnerId;
    private Long userId;
    private Long orderId;
    private LocalDate paymentDate;

    @JsonProperty("imp_uid")
    private String impUid;

    @JsonProperty("pay_method")
    private String payMethod;

    @JsonProperty("merchant_uid")
    private String merchantUid;

    @JsonProperty("paid_amount")
    private int paidAmount;

    @JsonProperty("pg_provider")
    private String pgProvider;

    @JsonProperty("pg_type")
    private String pgType;

    @JsonProperty("pg_tid")
    private String pgTid;
    private String status;
    @JsonProperty("card_name")
    private String cardName;
    @JsonProperty("card_number")
    private String cardNumber;
}
