package com.beust.jcommander;

public interface IUsageFormatter {
  void usage(String paramString);
  
  void usage(String paramString, StringBuilder paramStringBuilder);
  
  void usage(StringBuilder paramStringBuilder);
  
  void usage(String paramString1, StringBuilder paramStringBuilder, String paramString2);
  
  void usage(StringBuilder paramStringBuilder, String paramString);
  
  String getCommandDescription(String paramString);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\IUsageFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */