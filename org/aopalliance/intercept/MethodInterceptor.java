package org.aopalliance.intercept;

public interface MethodInterceptor extends Interceptor {
  Object invoke(MethodInvocation paramMethodInvocation) throws Throwable;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\aopalliance\intercept\MethodInterceptor.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */