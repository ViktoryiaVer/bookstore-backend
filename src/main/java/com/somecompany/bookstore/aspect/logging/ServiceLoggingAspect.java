package com.somecompany.bookstore.aspect.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class ServiceLoggingAspect {
    private final ObjectMapper mapper;

    @Pointcut("within(com.somecompany.bookstore.service.*) " +
            "&& @annotation(com.somecompany.bookstore.aspect.logging.annotation.LogInvocation)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object proceed = joinPoint.proceed();

        stopWatch.stop();

        log.info("Calling service method " + methodName + " of class " + className + " with arguments: " + mapper.writeValueAsString(args) +
                ". Execution time in milliseconds: " + stopWatch.getTotalTimeMillis());
        return proceed;
    }
}
