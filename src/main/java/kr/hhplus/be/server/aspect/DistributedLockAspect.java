package kr.hhplus.be.server.aspect;

import java.util.ArrayList;
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
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 10)  
public class DistributedLockAspect {

	private final RedissonClient redissonClient;
	private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser parser = new SpelExpressionParser();
	
	public DistributedLockAspect(RedissonClient redissonClient){
		this.redissonClient = redissonClient;
	}

	@Around("@annotation(distributedLock)")
	public Object lockAop(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
		List<RLock> locks = new ArrayList<>();

        // keys 배열에서 실제 값 가져와 RLock 생성
        for (String keyName : distributedLock.keys()) {
            String value = getName(joinPoint, keyName); // joinPoint에서 실제 값 추출
            RLock lock = redissonClient.getLock(distributedLock.type() + ":" + value);
            locks.add(lock);
        }

        // MultiLock 생성
        RLock multiLock = new RedissonMultiLock(locks.toArray(new RLock[0]));
        
	    try {
	        boolean multiPass = multiLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
	        Object result;
	        if (multiPass) {
	        	result = joinPoint.proceed();
	        } else {
	            throw new RuntimeException("Lock 획득 실패");
	        }
	        return result;
	    } finally {
	    	if (multiLock.isHeldByCurrentThread()) {
	    	    multiLock.unlock();
	    	}
	    }
	}
	
	private String getName(final ProceedingJoinPoint joinPoint, final String spelExpression) {
	    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	    Object[] args = joinPoint.getArgs();

	    // 파라미터 이름 가져오기
	    String[] paramNames = nameDiscoverer.getParameterNames(signature.getMethod());

	    // null이면 p0, p1, ... 형태로 fallback
	    EvaluationContext context = new StandardEvaluationContext();
	    for (int i = 0; i < args.length; i++) {
            context.setVariable("p" + i, args[i]); // #p0, #p1 ...
        }

	    // SpEL 파서
	    Object value = parser.parseExpression(spelExpression).getValue(context);
	    if (value == null) {
	        throw new RuntimeException("SpEL 평가 결과가 null입니다. expr=" + spelExpression);
	    }
	    return value.toString();
	}

}
