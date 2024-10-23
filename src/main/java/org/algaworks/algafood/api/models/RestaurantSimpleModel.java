package org.algaworks.algafood.api.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantSimpleModel {

    private Long id;
    private String name;
    private String kitchenName;
    private BigDecimal shippingFee;
    private Boolean isActive;
    private Boolean isOpen;
}