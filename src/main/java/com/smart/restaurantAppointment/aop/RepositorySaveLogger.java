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

//  Aspect - is the class that contains cross-cutting logic.
//  JointPoint - A point in a program where an action can be taken.
//  Pointcut - An expression that defines where the advice should be applied.
//  Advice- The action taken by an aspect at a particular jointPoint.(Before,After,Around,After returning,After throwing)
//  Weaving -

    @Pointcut("execution(* com.smart.restaurantAppointment.repository..*(..))")
    public void repositoryMethods() {}

    // 🔸 Around advice to log execution time
    @Around("repositoryMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("⏱️ [Repository TIME] {} took {} ms", joinPoint.getSignature().toShortString(), duration);
            return result;
        } catch(Throwable ex) {
            log.error("❌ [{}] failed | {}", joinPoint.getSignature().toShortString(), ex.getMessage());
            throw ex;
        }
    }
}
