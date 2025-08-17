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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import kr.hhplus.be.server.annotation.DistributedLock;
import kr.hhplus.be.server.order.dto.OrderRequest;


@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DistributedLockAspect {

	private final RedissonClient redissonClient;
    private final LockKeyGenerator lockKeyGenerator;

    public DistributedLockAspect(RedissonClient redissonClient, LockKeyGenerator lockKeyGenerator) {
        this.redissonClient = redissonClient;
        this.lockKeyGenerator = lockKeyGenerator;
    }

    @Around("execution(* kr.hhplus.be.server.order.service.OrderFacade.order(..)) && args(requestDto)")
    public Object lockAop(ProceedingJoinPoint joinPoint, OrderRequest requestDto) throws Throwable {

    	MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        if (distributedLock == null) {
            return joinPoint.proceed();
        }

        List<RLock> locks = new ArrayList<>();
        String[] keyValue = lockKeyGenerator.generateKey(joinPoint, distributedLock);
        for(String key : keyValue) {
        	RLock lock = redissonClient.getLock(key);
            locks.add(lock);
        }
        
        locks.sort(Comparator.comparing(RLock::getName));

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
}
