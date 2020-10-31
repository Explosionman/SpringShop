package ru.rybinskov.gb.springshop.shop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MeasureAspect {

    @Pointcut("@annotation(MeasureMethod)")
    private void annotatedNethod(){
    }

    @Around("annotatedNethod()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        long timeBefore = System.currentTimeMillis();
        Object value = pjp.proceed();
        long timeAfter = System.currentTimeMillis();
        System.out.println("Log measure of method" +
                pjp.toLongString() +
                " time mills: " + (timeAfter - timeBefore));
        return value;
    }

}
