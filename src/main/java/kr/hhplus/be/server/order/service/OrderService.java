package kr.hhplus.be.server.order.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.order.dto.OrderResponse;
import kr.hhplus.be.server.order.entity.OrderEntity;
import kr.hhplus.be.server.order.repository.OrderRepository;
import kr.hhplus.be.server.product.entity.ProductEntity;
import kr.hhplus.be.server.user.entity.UserEntity;

@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
    private final RedisTemplate<String, OrderResponse> redisTemplate;

    public OrderService(OrderRepository orderRepository,
                        RedisTemplate<String, OrderResponse> redisTemplate) {
        this.orderRepository = orderRepository;
        this.redisTemplate = redisTemplate;
    }

	public OrderEntity insertOrder(OrderEntity order) {
		return orderRepository.insertOrderInfo(order);
	}

	//주문생성
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public OrderEntity createOrder(UserEntity userInfo, ProductEntity buyProduct, int count, BigDecimal totalPrice, Long couponId) throws Exception {
		//주문 테이블에 insert
		OrderEntity order = OrderEntity.toEntity(userInfo, buyProduct, count, totalPrice, couponId);
		return this.insertOrder(order);
	}

	//주문조회
	public OrderResponse getOrderInfo(Long orderId) {
		String cacheKey = "order:" + orderId;

        // 1. Redis 캐시에서 조회
        OrderResponse cachedOrder = redisTemplate.opsForValue().get(cacheKey);
        if (cachedOrder != null) {
            return cachedOrder; // 캐시에 있으면 바로 반환
        }

        // 2. 캐시에 없으면 DB 조회
		Optional<OrderEntity> order = orderRepository.getOrderInfo(orderId);
		if(order.isPresent()) {
			OrderEntity entity = order.get();
			
			OrderResponse dto = new OrderResponse(entity.getGoodsId(), entity.getUserId(), entity.getCouponId(), entity.getCount(),
					entity.getOrderPrice(), entity.getPayPrice(), entity.getOrderState(), entity.getOrderDate());
			
			// 3. Redis에 저장 (예: TTL 60초)
            redisTemplate.opsForValue().set(cacheKey, dto, Duration.ofHours(1));
			return dto;
		}
		return null;
	}

}
