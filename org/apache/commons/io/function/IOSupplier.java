package org.apache.commons.io.function;

import java.io.IOException;

@FunctionalInterface
public interface IOSupplier<T> {
  T get() throws IOException;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\io\function\IOSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */