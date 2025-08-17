package kr.hhplus.be.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import kr.hhplus.be.server.annotation.DistributedLock;

public interface LockKeyGenerator {
    String[] generateKey(ProceedingJoinPoint joinPoint, DistributedLock lock);
}
