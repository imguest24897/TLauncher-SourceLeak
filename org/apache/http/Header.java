package org.apache.http;

public interface Header extends NameValuePair {
  HeaderElement[] getElements() throws ParseException;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\http\Header.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */