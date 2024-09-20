package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use an instance of one of the Finalizable*Reference classes")
@GwtIncompatible
public interface FinalizableReference {
  void finalizeReferent();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\base\FinalizableReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */