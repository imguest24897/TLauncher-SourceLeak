package org.apache.http.protocol;

@Deprecated
public interface ExecutionContext {
  public static final String HTTP_CONNECTION = "http.connection";
  
  public static final String HTTP_REQUEST = "http.request";
  
  public static final String HTTP_RESPONSE = "http.response";
  
  public static final String HTTP_TARGET_HOST = "http.target_host";
  
  @Deprecated
  public static final String HTTP_PROXY_HOST = "http.proxy_host";
  
  public static final String HTTP_REQ_SENT = "http.request_sent";
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\http\protocol\ExecutionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */