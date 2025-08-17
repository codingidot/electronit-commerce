package kr.hhplus.be.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import kr.hhplus.be.server.annotation.DistributedLock;
import kr.hhplus.be.server.order.dto.OrderRequest;

@Component
public class OrderLockKeyGenerator implements LockKeyGenerator {

    @Override
    public String[] generateKey(ProceedingJoinPoint joinPoint, DistributedLock lock) {
        Object[] args = joinPoint.getArgs();
        String[] keyArr = new String[2];
        for (Object arg : args) {
            if (arg instanceof OrderRequest requestDto) {
                keyArr[0] = "order:" + "productId:" + requestDto.getGoodsId();
                keyArr[1] = "order:" + "userId:" + requestDto.getUserId();
                return keyArr;
            }
        }
        throw new IllegalArgumentException("OrderRequestDto 파라미터가 필요합니다.");
    }
}
