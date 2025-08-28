package kr.hhplus.be.server.order.event;

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderEventHandler {

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("✅ 주문 생성 이벤트 수신 - 주문ID: {}, 사용자ID: {}, 총액: {}",
                event.getOrderId(), event.getUserId(), event.getTotalPrice());

    }

}
