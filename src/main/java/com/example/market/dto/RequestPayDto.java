package com.example.market.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestPayDto {
    private Long orderUid;
    private String itemName;
    private int paymentPrice;
    private String buyerEmail;
    private String buyerName;
    private String buyerTel;
    private String buyerAddress;
    private String buyerPostcode;

    private RequestPayDto(Long orderUid, String itemName, int paymentPrice, String buyerEmail, String buyerName, String buyerTel, String buyerAddress, String buyerPostcode) {
        this.orderUid = orderUid;
        this.itemName = itemName;
        this.paymentPrice = paymentPrice;
        this.buyerEmail = buyerEmail;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddress = buyerAddress;
        this.buyerPostcode = buyerPostcode;
    }

    public static RequestPayDto of(Long orderUid, String itemName, int paymentPrice, String buyerEmail, String buyerName, String buyerTel, String buyerAddress, String buyerPostcode) {
        return new RequestPayDto(orderUid, itemName, paymentPrice, buyerEmail, buyerName, buyerTel, buyerAddress, buyerPostcode);
    }

}
