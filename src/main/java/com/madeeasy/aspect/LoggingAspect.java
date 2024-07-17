package com.madeeasy.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut(value = "execution(* com.madeeasy.*.*.*(..))")
    public void logMessage() {

    }

    @Around(value = "logMessage()")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("Before " + proceedingJoinPoint.getSignature());
        Object returnValue = null;
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("After " + proceedingJoinPoint.getSignature());
        return returnValue;
    }
}