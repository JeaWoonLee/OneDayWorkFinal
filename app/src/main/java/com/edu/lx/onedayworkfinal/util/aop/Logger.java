package com.edu.lx.onedayworkfinal.util.aop;

import android.util.Log;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

@Aspect
public class Logger {

    private  final String TAG = "Aspect-Logger";

    /*
     * AOP TargetMethod
     * 대상 메소드 : 모든 접근 제어자 / com.edu.lx.onedayworkfinal 의 하위 패키지 / 모든 클래스 / 모든 메소드
     */
    @Pointcut("execution(* com.edu.lx.onedayworkfinal..*(..))")
    public void targetMethod() {

    }

    /*
     * AOP BeforeTargetMethod
     * 대상 클래스와 메소드 명, args 정보 출력
     */
    @Before("targetMethod()")
    public void beforeTargetMethod(JoinPoint joinPoint) {
        Log.d(TAG,"beforeTargetMethod");

        // Make log message
        StringBuffer buffer = new StringBuffer();

        // append [class.method()]
        buffer.append(processJoinPoint(joinPoint));

        // append args
        Object[] arguments = joinPoint.getArgs();
        int argCount = 0;
        for (Object obj : arguments) {
            buffer.append("\n -arg" + argCount++ + " : ");
            // commons-lang : toStringBuilder
            buffer.append(ToStringBuilder.reflectionToString(obj));
        }
        Log.d(TAG,buffer.toString());

    }

    /*
     * AOP afterReturningTargetMethod
     * 대상 클래스가 return 한 Object / List<?> 가 있다면 로깅
     *
     */
    @AfterReturning(pointcut = "targetMethod()", returning = "returnValue")
    public void afterReturningTargetMethod(JoinPoint joinPoint, Object returnValue) {
        Log.d(TAG,"afterReturningTargetMethod");

        // Make log message
        StringBuffer buffer = new StringBuffer();

        // append [class.method()]
        buffer.append(processJoinPoint(joinPoint));

        // return 의 결과값이 List<> 와 Object 에 따라 다르게 로그메세지를 출력
        // List<> 인 경우
        if (returnValue instanceof List) {
            List<?> resultList = (List<?>) returnValue;
            //List 의 size 출력
            buffer.append("resultList size : " + resultList.size() + "\n");
            //item.toString() 출력
            for (Object item : resultList) buffer.append(ToStringBuilder.reflectionToString(item) + "\n");

        } else {
            // Object 인 경우
            buffer.append(ToStringBuilder.reflectionToString(returnValue));
        }

        Log.d(TAG,buffer.toString());

    }

    /*
     *  processJoinPoint
     *  joinPoint 에서 className 과 methodName 를 추출하여 로깅할 메세지를 생성
     *  @Param JoinPoint
     *  @return String
     */
    private String processJoinPoint(JoinPoint joinPoint) {

        // Get Target Class
        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        // Get Class Name
        String className = clazz.getSimpleName();
        // Get Method Name
        String methodName = joinPoint.getSignature().getName();

        return "["+className+"."+methodName+"()]==";
    }
}
