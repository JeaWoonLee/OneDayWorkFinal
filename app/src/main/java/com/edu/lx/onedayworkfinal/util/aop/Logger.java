package com.edu.lx.onedayworkfinal.util.aop;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Logger {

    private  final String TAG = "Aspect-Logger";

    /*
     * AOP TargetMethod
     * 대상 메소드 : 모든 접근 제어자 / com.edu.lx.onedayworkfinal 의 하위 패키지 / 모든 클래스 / 모든 메소드
     */

    //@Pointcut("execution(* android.view.View.OnClickListener.*(..))")
    @Pointcut("execution(* com.edu.lx.onedayworkfinal..*(..))")
    public void targetMethod() {

    }

    @Before("targetMethod()")
    public void before(JoinPoint joinPoint) {
        Log.i(TAG,"before");
    }


}
