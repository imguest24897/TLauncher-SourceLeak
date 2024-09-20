package org.apache.commons.text;

import java.text.Format;
import java.util.Locale;

@FunctionalInterface
public interface FormatFactory {
  Format getFormat(String paramString1, String paramString2, Locale paramLocale);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\FormatFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */