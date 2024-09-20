package by.gdev.http.download.service;

import by.gdev.util.model.download.Metadata;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface GsonService {
  <T> T getObject(String paramString, Class<T> paramClass, boolean paramBoolean) throws FileNotFoundException, IOException, NoSuchAlgorithmException;
  
  <T> T getObjectByUrls(List<String> paramList, Class<T> paramClass, boolean paramBoolean) throws IOException, NoSuchAlgorithmException;
  
  <T> T getLocalObject(List<String> paramList, Class<T> paramClass) throws IOException, NoSuchAlgorithmException;
  
  <T> T getObjectByUrls(List<String> paramList, String paramString, Class<T> paramClass, boolean paramBoolean) throws FileNotFoundException, IOException, NoSuchAlgorithmException;
  
  <T> T getObjectByUrls(List<String> paramList, List<Metadata> paramList1, Class<T> paramClass, boolean paramBoolean) throws FileNotFoundException, IOException, NoSuchAlgorithmException;
  
  <T> T getObjectWithoutSaving(String paramString, Class<T> paramClass) throws IOException;
  
  <T> T getObjectWithoutSaving(String paramString, Class<T> paramClass, Map<String, String> paramMap) throws IOException;
  
  <T> T getObjectWithoutSaving(String paramString, Type paramType) throws IOException;
  
  <T> T getObjectWithoutSaving(String paramString, Type paramType, Map<String, String> paramMap) throws IOException;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\service\GsonService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */