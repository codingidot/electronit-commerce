package kr.hhplus.be.server.order.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderEventHandler {

	@Async
	@EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("✅ 주문 생성 이벤트 수신 - 주문ID: {}, 사용자ID: {}, 총액: {}",
                event.getOrderId(), event.getUserId(), event.getTotalPrice());

    }

}
