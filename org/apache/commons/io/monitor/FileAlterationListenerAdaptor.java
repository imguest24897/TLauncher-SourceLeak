package org.apache.commons.io.monitor;

import java.io.File;

public class FileAlterationListenerAdaptor implements FileAlterationListener {
  public void onStart(FileAlterationObserver observer) {}
  
  public void onDirectoryCreate(File directory) {}
  
  public void onDirectoryChange(File directory) {}
  
  public void onDirectoryDelete(File directory) {}
  
  public void onFileCreate(File file) {}
  
  public void onFileChange(File file) {}
  
  public void onFileDelete(File file) {}
  
  public void onStop(FileAlterationObserver observer) {}
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\io\monitor\FileAlterationListenerAdaptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */