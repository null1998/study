package org.example.entity;

import java.math.BigDecimal;

/**
 * @author huang
 */
public class Order {
    private String orderType;
    private String orderId;
    private String orderName;

    private BigDecimal amt;

    public Order(String orderType, String orderId, String orderName, BigDecimal amt) {
        this.orderType = orderType;
        this.orderId = orderId;
        this.orderName = orderName;
        this.amt = amt;
    }

    public Order() {
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
}
