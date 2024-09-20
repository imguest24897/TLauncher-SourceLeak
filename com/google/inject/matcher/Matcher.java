package com.google.inject.matcher;

public interface Matcher<T> {
  boolean matches(T paramT);
  
  Matcher<T> and(Matcher<? super T> paramMatcher);
  
  Matcher<T> or(Matcher<? super T> paramMatcher);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\matcher\Matcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */