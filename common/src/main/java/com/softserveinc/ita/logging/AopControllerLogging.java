package com.softserveinc.ita.logging;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;

@Aspect
public class AopControllerLogging {
    private final Logger logger = Logger.getLogger(getClass());
    private MethodSignature signature = null;
    private String[] requestMappingValues = null;
    private StringBuilder logMessage = null;
    private String controllerURL = "";
    private String requestMappingMethod = "";
    private String methodURL = "";
    private Object[] realParameters;

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void beanAnnotatedWithController() {
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void methodWithAnnotationRequestMapping() {
    }

    @Pointcut("publicMethod() && methodWithAnnotationRequestMapping() && beanAnnotatedWithController()")
    public void publicMethodWithAnnotationRequestMappingInsideAClassMarkedWithController() {
    }

    @Around("publicMethodWithAnnotationRequestMappingInsideAClassMarkedWithController()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        controllerURL = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class).value()[0];
        signature = (MethodSignature) joinPoint.getSignature();
        requestMappingValues = signature.getMethod().getAnnotation(RequestMapping.class).value();
        logMessage = new StringBuilder();
        realParameters = joinPoint.getArgs();
        requestMappingMethod = signature.getMethod().getAnnotation(RequestMapping.class).method()[0].toString();
        if (requestMappingValues.length != 0) {
            methodURL = requestMappingValues[0];
        }
        logMessage.append("-----START SNIPPET-----\n");
        logMessage.append(requestMappingMethod + " request to the URL: " + controllerURL + methodURL + "\n");
        if (realParameters.length != 0) {
            logMessage.append("Passed parameters is (are): ");
            for (Object o : realParameters) {
                logMessage.append(o.toString() + " \n");
            }
        } else {
            logMessage.append("no parameters have been passed\n");
        }
        Object obj = joinPoint.proceed();
        logMessage.append("-----END SNIPPET-----");
        logger.info(logMessage.toString());
        System.out.println(logMessage.toString());
        return obj;
    }
}