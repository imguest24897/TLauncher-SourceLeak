/*     */ package ch.qos.logback.core.rolling.helper;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileFilterUtil
/*     */ {
/*     */   public static void sortFileArrayByName(File[] fileArray) {
/*  26 */     Arrays.sort(fileArray, new Comparator<File>() {
/*     */           public int compare(File o1, File o2) {
/*  28 */             String o1Name = o1.getName();
/*  29 */             String o2Name = o2.getName();
/*  30 */             return o1Name.compareTo(o2Name);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static void reverseSortFileArrayByName(File[] fileArray) {
/*  36 */     Arrays.sort(fileArray, new Comparator<File>() {
/*     */           public int compare(File o1, File o2) {
/*  38 */             String o1Name = o1.getName();
/*  39 */             String o2Name = o2.getName();
/*  40 */             return o2Name.compareTo(o1Name);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static String afterLastSlash(String sregex) {
/*  46 */     int i = sregex.lastIndexOf('/');
/*  47 */     if (i == -1) {
/*  48 */       return sregex;
/*     */     }
/*  50 */     return sregex.substring(i + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEmptyDirectory(File dir) {
/*  55 */     if (!dir.isDirectory()) {
/*  56 */       throw new IllegalArgumentException("[" + dir + "] must be a directory");
/*     */     }
/*  58 */     String[] filesInDir = dir.list();
/*  59 */     if (filesInDir == null || filesInDir.length == 0) {
/*  60 */       return true;
/*     */     }
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File[] filesInFolderMatchingStemRegex(File file, final String stemRegex) {
/*  76 */     if (file == null) {
/*  77 */       return new File[0];
/*     */     }
/*  79 */     if (!file.exists() || !file.isDirectory()) {
/*  80 */       return new File[0];
/*     */     }
/*  82 */     return file.listFiles(new FilenameFilter() {
/*     */           public boolean accept(File dir, String name) {
/*  84 */             return name.matches(stemRegex);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static int findHighestCounter(File[] matchingFileArray, String stemRegex) {
/*  90 */     int max = Integer.MIN_VALUE;
/*  91 */     for (File aFile : matchingFileArray) {
/*  92 */       int aCounter = extractCounter(aFile, stemRegex);
/*  93 */       if (max < aCounter)
/*  94 */         max = aCounter; 
/*     */     } 
/*  96 */     return max;
/*     */   }
/*     */   
/*     */   public static int extractCounter(File file, String stemRegex) {
/* 100 */     Pattern p = Pattern.compile(stemRegex);
/* 101 */     String lastFileName = file.getName();
/*     */     
/* 103 */     Matcher m = p.matcher(lastFileName);
/* 104 */     if (!m.matches()) {
/* 105 */       throw new IllegalStateException("The regex [" + stemRegex + "] should match [" + lastFileName + "]");
/*     */     }
/* 107 */     String counterAsStr = m.group(1);
/* 108 */     return (new Integer(counterAsStr)).intValue();
/*     */   }
/*     */   
/*     */   public static String slashify(String in) {
/* 112 */     return in.replace('\\', '/');
/*     */   }
/*     */ 
/*     */   
/*     */   public static void removeEmptyParentDirectories(File file, int recursivityCount) {
/* 117 */     if (recursivityCount >= 3) {
/*     */       return;
/*     */     }
/* 120 */     File parent = file.getParentFile();
/* 121 */     if (parent.isDirectory() && isEmptyDirectory(parent)) {
/* 122 */       parent.delete();
/* 123 */       removeEmptyParentDirectories(parent, recursivityCount + 1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\rolling\helper\FileFilterUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */