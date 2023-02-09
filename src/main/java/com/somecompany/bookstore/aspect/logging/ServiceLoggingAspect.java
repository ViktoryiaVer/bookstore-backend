package com.somecompany.bookstore.aspect.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class ServiceLoggingAspect {
    private final ObjectMapper mapper;

    @Around("@annotation(com.somecompany.bookstore.aspect.logging.annotation.LogInvocation)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object proceed = joinPoint.proceed();

        stopWatch.stop();

        log.info("Calling service method {} of class {} with arguments: {}. Execution time in milliseconds: {}",
                methodName, className, mapper.writeValueAsString(args), stopWatch.getTotalTimeMillis());
        return proceed;
    }
}
