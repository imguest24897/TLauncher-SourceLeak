/*    */ package by.gdev.process;
/*    */ 
/*    */ import by.gdev.util.model.download.Metadata;
/*    */ import by.gdev.util.model.download.Repo;
/*    */ import com.google.common.eventbus.EventBus;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class JavaProcessHelper {
/* 19 */   public void setDirectory(File directory) { this.directory = directory; } public void setProcess(ProcessBuilder process) { this.process = process; } public void setListener(EventBus listener) { this.listener = listener; } public void setMonitor(ProcessMonitor monitor) { this.monitor = monitor; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JavaProcessHelper)) return false;  JavaProcessHelper other = (JavaProcessHelper)o; if (!other.canEqual(this)) return false;  Object this$jvmPath = getJvmPath(), other$jvmPath = other.getJvmPath(); if ((this$jvmPath == null) ? (other$jvmPath != null) : !this$jvmPath.equals(other$jvmPath)) return false;  Object<String> this$commands = (Object<String>)getCommands(), other$commands = (Object<String>)other.getCommands(); if ((this$commands == null) ? (other$commands != null) : !this$commands.equals(other$commands)) return false;  Object this$directory = getDirectory(), other$directory = other.getDirectory(); if ((this$directory == null) ? (other$directory != null) : !this$directory.equals(other$directory)) return false;  Object this$process = getProcess(), other$process = other.getProcess(); if ((this$process == null) ? (other$process != null) : !this$process.equals(other$process)) return false;  Object this$listener = getListener(), other$listener = other.getListener(); if ((this$listener == null) ? (other$listener != null) : !this$listener.equals(other$listener)) return false;  Object this$monitor = getMonitor(), other$monitor = other.getMonitor(); return !((this$monitor == null) ? (other$monitor != null) : !this$monitor.equals(other$monitor)); } protected boolean canEqual(Object other) { return other instanceof JavaProcessHelper; } public int hashCode() { int PRIME = 59; result = 1; Object $jvmPath = getJvmPath(); result = result * 59 + (($jvmPath == null) ? 43 : $jvmPath.hashCode()); Object<String> $commands = (Object<String>)getCommands(); result = result * 59 + (($commands == null) ? 43 : $commands.hashCode()); Object $directory = getDirectory(); result = result * 59 + (($directory == null) ? 43 : $directory.hashCode()); Object $process = getProcess(); result = result * 59 + (($process == null) ? 43 : $process.hashCode()); Object $listener = getListener(); result = result * 59 + (($listener == null) ? 43 : $listener.hashCode()); Object $monitor = getMonitor(); return result * 59 + (($monitor == null) ? 43 : $monitor.hashCode()); } public String toString() { return "JavaProcessHelper(jvmPath=" + getJvmPath() + ", commands=" + getCommands() + ", directory=" + getDirectory() + ", process=" + getProcess() + ", listener=" + getListener() + ", monitor=" + getMonitor() + ")"; }
/* 20 */    private final String jvmPath; private final List<String> commands; private File directory; private static final Logger log = LoggerFactory.getLogger(JavaProcessHelper.class); private ProcessBuilder process; private EventBus listener; ProcessMonitor monitor;
/*    */   public String getJvmPath() {
/* 22 */     return this.jvmPath; }
/* 23 */   public List<String> getCommands() { return this.commands; }
/* 24 */   public File getDirectory() { return this.directory; }
/* 25 */   public ProcessBuilder getProcess() { return this.process; }
/* 26 */   public EventBus getListener() { return this.listener; } public ProcessMonitor getMonitor() {
/* 27 */     return this.monitor;
/*    */   }
/*    */   public JavaProcessHelper(String jvmPath, File directory, EventBus listener) {
/* 30 */     this.jvmPath = jvmPath;
/* 31 */     this.directory = directory;
/* 32 */     this.listener = listener;
/* 33 */     this.commands = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public void start() throws IOException {
/* 37 */     this.monitor = new ProcessMonitor(createProcess().start(), this.listener);
/* 38 */     this.monitor.start();
/*    */   }
/*    */   
/*    */   public void destroyProcess() {
/* 42 */     this.monitor.getProcess().destroyForcibly();
/*    */   }
/*    */ 
/*    */   
/*    */   public ProcessBuilder createProcess() {
/* 47 */     if (Objects.isNull(this.process))
/* 48 */       this.process = (new ProcessBuilder(getFullCommands())).directory(this.directory).redirectErrorStream(true); 
/* 49 */     String javaOption = findJavaOptionAndGetName();
/* 50 */     if (Objects.nonNull(javaOption)) {
/* 51 */       this.process.environment().put(javaOption, "");
/*    */     }
/* 53 */     String runCommand = this.process.command().stream().collect(Collectors.joining(" "));
/* 54 */     log.info("start command {}", runCommand);
/* 55 */     return this.process;
/*    */   }
/*    */   
/*    */   private List<String> getFullCommands() {
/* 59 */     List<String> result = new ArrayList<>(this.commands);
/* 60 */     result.add(0, this.jvmPath);
/* 61 */     return result;
/*    */   }
/*    */   
/*    */   private static String findJavaOptionAndGetName() {
/* 65 */     for (Map.Entry<String, String> e : System.getenv().entrySet()) {
/* 66 */       if (((String)e.getKey()).equalsIgnoreCase("_java_options"))
/* 67 */         return e.getKey(); 
/* 68 */     }  return null;
/*    */   }
/*    */   
/*    */   public void addCommand(String command) {
/* 72 */     this.commands.add(command);
/*    */   }
/*    */   
/*    */   public void addCommand(String key, String value) {
/* 76 */     this.commands.add(key);
/* 77 */     this.commands.add(value);
/*    */   }
/*    */   
/*    */   public void addCommands(List<String> list) {
/* 81 */     for (String c : list)
/* 82 */       this.commands.add(c); 
/*    */   }
/*    */   
/*    */   public List<Path> librariesForRunning(String workDirectory, Repo fileRepo, Repo dependencis) {
/* 86 */     List<Path> list = new ArrayList<>();
/* 87 */     dependencis.getResources().forEach(dep -> list.add(Paths.get(workDirectory, new String[] { dep.getPath() })));
/*    */ 
/*    */     
/* 90 */     fileRepo.getResources().forEach(core -> list.add(Paths.get(workDirectory, new String[] { core.getPath() })));
/*    */ 
/*    */     
/* 93 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\process\JavaProcessHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */