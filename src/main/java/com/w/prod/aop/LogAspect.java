package com.w.prod.aop;

import com.w.prod.models.binding.IdeaAddBindingModel;
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

    @Pointcut("execution(* com.w.prod.web.IdeaController.postIdea(..))")
    public void ideaCreatePointCut() {
    }

    @After("joinPointCut()")
    public void joinProductAfterAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String productId = (String) args[0];
        String action = joinPoint.getSignature().getName();

        logService.createProductJoinLog(action, productId);
    }

    @After("ideaCreatePointCut()")
    public void ideaCreateAfterAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String ideaName = ((IdeaAddBindingModel) args[0]).getName();
        String action = joinPoint.getSignature().getName();

        logService.createIdeaAddLog(action, ideaName);
    }
}


