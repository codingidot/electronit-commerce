package kr.hhplus.be.server.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import kr.hhplus.be.server.annotation.DistributedLock;
import kr.hhplus.be.server.order.dto.OrderRequestDto;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DistributedLockAspect {

	private final RedissonClient redissonClient;
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser parser = new SpelExpressionParser();

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("execution(* kr.hhplus.be.server.order.service.OrderFacade.order(..)) && args(requestDto)")
    public Object lockAop(ProceedingJoinPoint joinPoint, OrderRequestDto requestDto) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        if (distributedLock == null) {
            return joinPoint.proceed(); // 어노테이션 없으면 그냥 진행
        }

        List<RLock> locks = new ArrayList<>();
        for (String spelKey : distributedLock.keys()) {
            String keyValue = parseSpel(joinPoint, method, spelKey, requestDto);
            RLock lock = redissonClient.getLock(distributedLock.type() + ":" + keyValue);
            locks.add(lock);
        }

        // Deadlock 방지: key 값 기준으로 정렬
        locks.sort((a, b) -> a.getName().compareTo(b.getName()));

        RLock multiLock = new RedissonMultiLock(locks.toArray(new RLock[0]));

        try {
            boolean acquired = multiLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!acquired) {
                throw new RuntimeException("Lock 획득 실패");
            }
            return joinPoint.proceed();
        } finally {
            if (multiLock.isHeldByCurrentThread()) {
                multiLock.unlock();
            }
        }
    }
	
	private String parseSpel(ProceedingJoinPoint joinPoint, Method method, String spelExpression, OrderRequestDto requestDto) {
        EvaluationContext context = new StandardEvaluationContext();
        // 파라미터 이름 가져오기
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();

        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        // requestDto 직접 바인딩도 가능
        context.setVariable("request", requestDto);

        Object value = parser.parseExpression(spelExpression).getValue(context);
        if (value == null) {
            throw new RuntimeException("SpEL 평가 결과가 null입니다. expr=" + spelExpression);
        }
        return value.toString();
    }
}
