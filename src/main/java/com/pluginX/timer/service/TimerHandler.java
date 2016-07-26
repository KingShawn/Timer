package com.pluginX.timer.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pluginX.timer.annotation.Timer;

/**
 * 
 * 对使用@Timer注解的方法进行计时运算 <br>
 *
 * @author Shawn Wang
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
@Aspect
public class TimerHandler {
    private Logger LOGGER;
    private Object target;
    private Method method;
    private Timer timer;

    @Around("@annotation(com.pluginX.timer.annotation.Timer)")
    public void timerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean flag = true;
        String methodName = null;
        try {
            methodName = joinPoint.getSignature().getName();
            target = joinPoint.getTarget();
            LOGGER = LoggerFactory.getLogger(target.getClass());
            // 得到拦截的方法
            method = getMethodByClassAndName(target.getClass(), methodName);
            // 目标方法的Timer注解
            timer = (Timer) getAnnotationByMethod(method, Timer.class);
        } catch (Exception e) {
            flag = false;
        } finally {
            // 验证通过
            if (flag) {
                if (timer.value() >= 1) {
                    StopWatch clock = new StopWatch();
                    clock.start(); // 计时开始
                    for (int i = 1; i <= timer.value(); i++) {
                        joinPoint.proceed();
                    }
                    clock.stop(); // 计时结束
                    LOGGER.debug(target.getClass() + "调用" + method.getName() + "耗时：" + clock.getTime() + "ms 执行次数："
                            + timer.value());
                }
            } else {
                //直接通过
                joinPoint.proceed();
            }
        }

    }

    /**
     * 
     * 功能描述:根据目标方法和注解类型 得到该目标方法的指定注解 <br>
     * 〈功能详细描述〉
     *
     * @param method
     * @param annoClass
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @SuppressWarnings("rawtypes")
    private Annotation getAnnotationByMethod(Method method, Class annoClass) {
        Annotation all[] = method.getAnnotations();
        for (Annotation annotation : all) {
            if (annotation.annotationType() == annoClass) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 
     * 功能描述:根据类和方法名得到方法 <br>
     *
     * @param c
     * @param methodName
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @SuppressWarnings("rawtypes")
    private Method getMethodByClassAndName(Class c, String methodName) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

}
