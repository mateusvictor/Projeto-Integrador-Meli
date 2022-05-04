package br.com.meli.fresh.model;

import org.aspectj.weaver.ast.Or;

public enum OrderStatus {
    PREPARING("PREPARING"),
    ON_THE_WAY("ON_THE_WAY"),
    FINISHED("FINISHED"),
    CANCELLED("CANCELLED");

    public final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
