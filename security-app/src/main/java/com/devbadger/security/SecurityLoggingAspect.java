package com.devbadger.security;

import com.google.common.base.Joiner;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Component
public class SecurityLoggingAspect {

    @Around("execution(* com.devbadger.security.controller.*.*(..))")
    public Object logger(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object value = point.proceed();
        stopWatch.stop();
        ResponseEntity response = (ResponseEntity) value;

        StringBuffer sb = new StringBuffer();
        sb.append("methodName=\"").append(escape(point.getSignature().getName())).append("\" ");
        sb.append("arguments=\"").append(escape(getArguments(point.getArgs()))).append("\" ");
        sb.append("executionTime=\"").append(stopWatch.getTotalTimeMillis()).append("\" ");
        sb.append("responseCode=\"").append(response.getStatusCode()).append("\" ");

        LoggerFactory.getLogger(point.getSignature().getDeclaringTypeName()).info(sb.toString());

        return value;
    }

    @AfterThrowing(pointcut="execution(* com.devbadger.security.controller.*.*(..))",
            throwing="ex")
    public void exceptionLogger(JoinPoint point, Throwable ex) {
        StringBuffer sb = new StringBuffer();
        sb.append("methodNameThatThrew=\"").append(escape(point.getSignature().getName())).append("\" ");
        sb.append("arguments=\"").append(escape(getArguments(point.getArgs()))).append("\" ");
        sb.append("exceptionMessage=\"").append(escape(ex.toString())).append("\" ");

        LoggerFactory.getLogger(point.getSignature().getDeclaringTypeName()).error(sb.toString());
    }

    private String getArguments(Object[] arguments) {

        String argumentsAsString = null;
        if(!ArrayUtils.isEmpty(arguments)) {
            argumentsAsString = Joiner.on(",").join(arguments);
        }

        return argumentsAsString;
    }

    private String escape(final String value) {
        if(StringUtils.isBlank(value)) {
            return value;
        }

        return StringEscapeUtils.escapeXml(value);
    }
}
