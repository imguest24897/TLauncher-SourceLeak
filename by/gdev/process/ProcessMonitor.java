/*    */ package by.gdev.process;
/*    */ 
/*    */ import by.gdev.model.ExceptionMessage;
/*    */ import by.gdev.model.StarterAppProcess;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import com.google.common.eventbus.EventBus;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.Objects;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProcessMonitor
/*    */   extends Thread
/*    */ {
/* 24 */   private static final Logger log = LoggerFactory.getLogger(ProcessMonitor.class); private Process process;
/*    */   public Process getProcess() {
/* 26 */     return this.process;
/*    */   }
/*    */   private EventBus listener;
/*    */   
/*    */   public ProcessMonitor(Process process, EventBus listener) {
/* 31 */     this.process = process;
/* 32 */     this.listener = listener;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 37 */     InputStreamReader reader = new InputStreamReader(this.process.getInputStream());
/* 38 */     BufferedReader buf = new BufferedReader(reader);
/*    */     
/* 40 */     while (this.process.isAlive()) {
/*    */       try {
/* 42 */         String line; while (Objects.nonNull(line = buf.readLine())) {
/* 43 */           StarterAppProcess status = new StarterAppProcess();
/* 44 */           status.setLine(line);
/* 45 */           status.setProcess(this.process);
/* 46 */           this.listener.post(status);
/*    */         } 
/* 48 */       } catch (IOException t) {
/* 49 */         DesktopUtil.sleep(1);
/* 50 */         StarterAppProcess statusError = new StarterAppProcess();
/* 51 */         statusError.setProcess(this.process);
/* 52 */         statusError.setExeption(t);
/* 53 */         statusError.setLine("error");
/* 54 */         this.listener.post(statusError);
/*    */         try {
/* 56 */           int exitValue = this.process.exitValue();
/* 57 */           statusError.setErrorCode(Integer.valueOf(exitValue));
/* 58 */         } catch (IllegalThreadStateException s) {
/* 59 */           this.listener.post(new ExceptionMessage(s.getMessage()));
/* 60 */           statusError.setErrorCode(Integer.valueOf(-3));
/* 61 */           log.warn("warn", s);
/*    */         } 
/*    */       } finally {
/*    */         try {
/* 65 */           IOUtils.close(buf);
/* 66 */         } catch (IOException e) {
/* 67 */           log.error("Error", e);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\process\ProcessMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */