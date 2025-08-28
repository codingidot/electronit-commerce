package kr.hhplus.be.server.order.event;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class OrderCreatedEvent {
    private final Long orderId;
    private final Long userId;
    private final BigDecimal totalPrice;

    public OrderCreatedEvent(Long orderId, Long userId, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }
}