package com.smart.restaurantAppointment.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RepositorySaveLogger {
    @Pointcut("execution(* com.smart.restaurantAppointment.repository..*(..))")
    public void repositoryMethods() {}

    // 🔸 Before executing a repository method
    @Before("repositoryMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("📥 [Repository CALL] {} | Args: {}", method, args);
    }

    // 🔸 After method successfully returns
    @AfterReturning(pointcut = "repositoryMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();
        log.info("✅ [Repository RETURN] {} | Result: {}", method, result);
    }

    // 🔸 If method throws exception
    @AfterThrowing(pointcut = "repositoryMethods()", throwing = "error")
    public void logError(JoinPoint joinPoint, Throwable error) {
        String method = joinPoint.getSignature().toShortString();
        log.error("❌ [Repository ERROR] {} | Message: {}", method, error.getMessage());
    }

    // 🔸 Around advice to log execution time
    @Around("repositoryMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("⏱️ [Repository TIME] {} took {} ms", joinPoint.getSignature().toShortString(), duration);
        return result;
    }
}
