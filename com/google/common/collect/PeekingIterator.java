package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.DoNotMock;
import java.util.Iterator;

@DoNotMock("Use Iterators.peekingIterator")
@GwtCompatible
public interface PeekingIterator<E> extends Iterator<E> {
  E peek();
  
  @CanIgnoreReturnValue
  E next();
  
  void remove();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\PeekingIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */