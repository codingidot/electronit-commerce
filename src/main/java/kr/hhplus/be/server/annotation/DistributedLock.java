package kr.hhplus.be.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

	String[] keys();                         // 락 키
	String type() default "";			  //각 테스트케이스 구분
    long waitTime() default 1L;           // 락 획득 대기 시간
    long leaseTime() default 5L;         // 락 임대 시간 (자동 해제 시간)
    TimeUnit timeUnit() default TimeUnit.SECONDS;  // 시간
}
