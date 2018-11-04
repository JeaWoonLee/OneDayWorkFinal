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
     * 대상 메소드 : 모든 접근 제어자 / com.edu.lx.onedayworkfinal 의 하위 패키지 / Activity & Fragment 클래스 / 모든 메소드
     */
    @Pointcut("execution(* com.edu.lx.onedayworkfinal..*Activity.*(..)) || execution(* com.edu.lx.onedayworkfinal..*Fragment.*(..))")
    public void targetMethod() {

    }

    /*
     * AOP BeforeTargetMethod
     * 대상 클래스와 메소드 명, args 정보 출력
     */
    @Before("targetMethod()")
    public void beforeTargetMethod(JoinPoint joinPoint) {

        //인터페이스 리스너 메소드가 실행되면 클래스 인스턴스를 갖고 올 수 없어서 에러가 난다
        if(joinPoint.getTarget() == null) return;

        // Make log message
        StringBuffer buffer = new StringBuffer();

        // append [class.method()]
        buffer.append("beforeTargetMethod="+processJoinPoint(joinPoint));

        // append args
        Object[] arguments = joinPoint.getArgs();
        int argCount = 0;
        for (Object obj : arguments) {

            buffer.append("\n -arg" + argCount++ + " : ");

            if(obj != null) buffer.append(obj.toString());
            else buffer.append("null");
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

        //인터페이스 리스너 메소드가 실행되면 클래스 인스턴스를 갖고 올 수 없어서 에러가 난다
        if (joinPoint.getTarget() == null )return;
        //리턴값이 없다면 AfterReturning 의 로그를 추가로 찍어 줄 필요가 없다
        else if (returnValue == null) return;;
        // Make log message
        StringBuffer buffer = new StringBuffer();

        // append [class.method()]
        buffer.append("afterReturningTargetMethod=="+processJoinPoint(joinPoint));

        // return 의 결과값이 List<> 와 Object 에 따라 다르게 로그메세지를 출력
        // List<> 인 경우
        if (returnValue instanceof List) {
            List<?> resultList = (List<?>) returnValue;
            //List 의 size 출력
            buffer.append("resultList size : " + resultList.size() + "\n");
            //item.toString() 출력
            for (Object item : resultList) buffer.append(item.toString());

        } else {
            // Object 인 경우
            if(returnValue != null) buffer.append(returnValue.toString());
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
        @SuppressWarnings("unused")
        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        // Get Class Name
        String className = clazz.getSimpleName();
        // Get Method Name
        String methodName = joinPoint.getSignature().getName();

        return "["+className+"."+methodName+"()]==";
    }
}
