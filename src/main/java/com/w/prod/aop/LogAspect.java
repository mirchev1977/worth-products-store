package com.w.prod.aop;

import com.w.prod.models.binding.BlueprintAddBindingModel;
import com.w.prod.services.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private final LogService logService;

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(* com.w.prod.web.ProductController.joinProduct(..))")
    public void joinPointCut() {
    }

    @Pointcut("execution(* com.w.prod.web.BlueprintController.postBlueprint(..))")
    public void blueprintCreatePointCut() {
    }

    @After("joinPointCut()")
    public void joinProductAfterAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String productId = (String) args[0];
        String action = joinPoint.getSignature().getName();

        logService.createProductJoinLog(action, productId);
    }

    @After("blueprintCreatePointCut()")
    public void blueprintCreateAfterAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String blueprintName = ((BlueprintAddBindingModel) args[0]).getName();
        String action = joinPoint.getSignature().getName();

        logService.createBlueprintAddLog(action, blueprintName);
    }
}


