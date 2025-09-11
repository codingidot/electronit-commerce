package kr.hhplus.be.server;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import kr.hhplus.be.server.order.event.OrderCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;


@SpringBootTest
public class KafkaOrderTest {

    @Autowired
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;


    @Test
    void sendOrderCreatedEvent() throws InterruptedException, ExecutionException {
        OrderCreatedEvent event = new OrderCreatedEvent(1L, 2L, BigDecimal.valueOf(1000));
        kafkaTemplate.send("order-event", event).get();
    }
}