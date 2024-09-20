package ch.qos.logback.core.net.server;

import java.io.Closeable;

public interface Client extends Runnable, Closeable {
  void close();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\net\server\Client.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */