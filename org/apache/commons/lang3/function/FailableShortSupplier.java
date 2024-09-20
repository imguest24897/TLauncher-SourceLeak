package org.apache.commons.lang3.function;

@FunctionalInterface
public interface FailableShortSupplier<E extends Throwable> {
  short getAsShort() throws E;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\function\FailableShortSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */