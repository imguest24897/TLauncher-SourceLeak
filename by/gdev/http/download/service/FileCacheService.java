package by.gdev.http.download.service;

import by.gdev.util.model.download.Metadata;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FileCacheService {
  Path getRawObject(String paramString, boolean paramBoolean) throws IOException, NoSuchAlgorithmException;
  
  Path getRawObject(List<String> paramList, boolean paramBoolean) throws IOException, NoSuchAlgorithmException;
  
  Path getRawObject(List<String> paramList, Metadata paramMetadata, boolean paramBoolean) throws IOException, NoSuchAlgorithmException;
  
  Path getRawObject(List<String> paramList) throws IOException, NoSuchAlgorithmException;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\service\FileCacheService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */