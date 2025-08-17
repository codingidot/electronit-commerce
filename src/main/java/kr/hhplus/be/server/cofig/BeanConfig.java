package kr.hhplus.be.server.cofig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import kr.hhplus.be.server.order.dto.OrderResponseDto;

@Configuration
public class BeanConfig {

	@Bean
    public RedisTemplate<String, OrderResponseDto> orderResponseRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, OrderResponseDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(OrderResponseDto.class));

        return template;
    }
}
