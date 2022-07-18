package com.spaceobj.advertise.aop;

import com.spaceobj.advertise.utils.ResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;

/**
 * @author zhr_java@163.com
 * @date 2022/5/28 21:59
 */
public class ServiceProceedingJoinPoint implements ProceedingJoinPoint {

    ResultData result = null;

    public ServiceProceedingJoinPoint(ResultData content) {

        this.result = content;
    }

    @Override
    public void set$AroundClosure(AroundClosure aroundClosure) {

    }

    @Override
    public Object proceed() throws Throwable {

        return result;
    }


    @Override
    public Object proceed(Object[] objects) throws Throwable {

        return null;
    }

    @Override
    public String toShortString() {

        return null;
    }

    @Override
    public String toLongString() {

        return null;
    }

    @Override
    public Object getThis() {

        return null;
    }

    @Override
    public Object getTarget() {

        return null;
    }

    @Override
    public Object[] getArgs() {

        return new Object[0];
    }

    @Override
    public Signature getSignature() {

        return null;
    }

    @Override
    public SourceLocation getSourceLocation() {

        return null;
    }

    @Override
    public String getKind() {

        return null;
    }

    @Override
    public StaticPart getStaticPart() {

        return null;
    }

}
