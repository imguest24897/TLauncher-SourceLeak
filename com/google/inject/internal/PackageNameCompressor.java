/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Ordering;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PackageNameCompressor
/*     */ {
/*     */   static final String LEGEND_HEADER = "\n\n======================\nFull classname legend:\n======================\n";
/*     */   static final String LEGEND_FOOTER = "========================\nEnd of classname legend:\n========================\n";
/*  50 */   private static final ImmutableSet<String> PACKAGES_SKIPPED_IN_LEGEND = ImmutableSet.of("java.lang.", "java.util.");
/*     */ 
/*     */ 
/*     */   
/*  54 */   private static final Splitter PACKAGE_SPLITTER = Splitter.on('.');
/*     */   
/*  56 */   private static final Joiner PACKAGE_JOINER = Joiner.on('.');
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
/*  68 */   private static final Pattern CLASSNAME_PATTERN = Pattern.compile("[\\W](([a-z_0-9]++[.]){2,}+[A-Z][\\w$]*)");
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
/*  82 */   private static final Pattern QUOTED_PATTERN = Pattern.compile("([^\\\"]+)((\\\")?[^\\\"\\r\\n]*\\\")?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String compressPackagesInMessage(String input) {
/*  89 */     Matcher matcher = CLASSNAME_PATTERN.matcher(input);
/*     */     
/*  91 */     Set<String> names = new HashSet<>();
/*     */ 
/*     */     
/*  94 */     while (matcher.find()) {
/*  95 */       String name = matcher.group(1);
/*  96 */       names.add(name);
/*     */     } 
/*     */ 
/*     */     
/* 100 */     Map<String, String> replacementMap = shortenNames(names);
/*     */ 
/*     */     
/* 103 */     if (replacementMap.isEmpty()) {
/* 104 */       return input;
/*     */     }
/*     */     
/* 107 */     StringBuilder output = new StringBuilder();
/* 108 */     ImmutableSet<String> immutableSet = replaceFullNames(input, replacementMap, output);
/* 109 */     if (immutableSet.isEmpty()) {
/* 110 */       return input;
/*     */     }
/*     */ 
/*     */     
/* 114 */     Objects.requireNonNull(immutableSet); String classNameLegend = buildClassNameLegend(Maps.filterKeys(replacementMap, immutableSet::contains));
/* 115 */     return output.append(classNameLegend).toString();
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
/*     */   private static ImmutableSet<String> replaceFullNames(String input, Map<String, String> replacementMap, StringBuilder output) {
/* 128 */     ImmutableSet.Builder<String> replacedShortNames = ImmutableSet.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     List<String> shortNames = (List<String>)replacementMap.keySet().stream().sorted((Comparator)Ordering.natural().reverse()).collect(Collectors.toList());
/* 136 */     Matcher matcher = QUOTED_PATTERN.matcher(input);
/* 137 */     while (matcher.find()) {
/* 138 */       String replaced = matcher.group(1);
/* 139 */       for (String shortName : shortNames) {
/* 140 */         String fullName = replacementMap.get(shortName);
/* 141 */         int beforeLen = replaced.length();
/* 142 */         replaced = replaced.replace(fullName, shortName);
/*     */ 
/*     */         
/* 145 */         if (replaced.length() < beforeLen) {
/* 146 */           replacedShortNames.add(shortName);
/*     */         }
/*     */       } 
/* 149 */       output.append(replaced);
/* 150 */       String quoted = matcher.group(2);
/* 151 */       if (quoted != null) {
/* 152 */         output.append(quoted);
/*     */       }
/*     */     } 
/* 155 */     return replacedShortNames.build();
/*     */   }
/*     */   
/*     */   private static String buildClassNameLegend(Map<String, String> replacementMap) {
/* 159 */     StringBuilder legendBuilder = new StringBuilder();
/*     */     
/* 161 */     int longestKey = ((String)replacementMap.keySet().stream().max(Comparator.comparing(String::length)).get()).length();
/* 162 */     for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
/* 163 */       String shortName = entry.getKey();
/* 164 */       String fullName = entry.getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 170 */       String prefix = fullName.substring(0, fullName.length() - shortName.length());
/* 171 */       if (PACKAGES_SKIPPED_IN_LEGEND.contains(prefix) && !shortName.contains(".")) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 176 */       legendBuilder
/* 177 */         .append(shortName)
/* 178 */         .append(": ")
/*     */         
/* 180 */         .append(Strings.repeat(" ", longestKey - shortName.length()))
/*     */ 
/*     */         
/* 183 */         .append('"')
/* 184 */         .append(fullName)
/* 185 */         .append('"')
/* 186 */         .append("\n");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 191 */     String str1 = Messages.bold(
/*     */         
/* 193 */         "\n\n======================\nFull classname legend:\n======================\n"); String str2 = Messages.faint(legendBuilder.toString()); String str3 = Messages.bold("========================\nEnd of classname legend:\n========================\n"); return (legendBuilder.length() == 0) ? "" : (new StringBuilder(String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length())).append(str1).append(str2).append(str3).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<String, String> shortenNames(Collection<String> names) {
/* 202 */     HashMultimap<String, List<String>> shortNameToPartsMap = HashMultimap.create();
/* 203 */     for (String name : names) {
/* 204 */       List<String> parts = new ArrayList<>(PACKAGE_SPLITTER.splitToList(name));
/*     */       
/* 206 */       String className = parts.remove(parts.size() - 1);
/* 207 */       shortNameToPartsMap.put(className, parts);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 214 */       List<String> conflictingShortNames = new ArrayList<>();
/*     */       
/* 216 */       for (Map.Entry<String, Collection<List<String>>> entry : (Iterable<Map.Entry<String, Collection<List<String>>>>)shortNameToPartsMap.asMap().entrySet()) {
/* 217 */         if (((Collection)entry.getValue()).size() > 1) {
/* 218 */           conflictingShortNames.add(entry.getKey());
/*     */         }
/*     */       } 
/*     */       
/* 222 */       if (conflictingShortNames.isEmpty()) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 227 */       for (String conflictingShortName : conflictingShortNames) {
/* 228 */         Set<List<String>> partsCollection = shortNameToPartsMap.removeAll(conflictingShortName);
/* 229 */         for (List<String> parts : partsCollection) {
/* 230 */           String str1 = parts.remove(parts.size() - 1), newShortName = (new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(conflictingShortName).length())).append(str1).append(".").append(conflictingShortName).toString();
/*     */ 
/*     */           
/* 233 */           if (!parts.isEmpty()) {
/* 234 */             shortNameToPartsMap.put(newShortName, parts);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 242 */     Map<String, String> replacementMap = new TreeMap<>();
/*     */     
/* 244 */     for (Map.Entry<String, Collection<List<String>>> entry : (Iterable<Map.Entry<String, Collection<List<String>>>>)shortNameToPartsMap.asMap().entrySet()) {
/*     */ 
/*     */       
/* 247 */       String str1 = PACKAGE_JOINER.join((Iterable)Iterables.getOnlyElement(entry.getValue())), str2 = entry.getKey(); replacementMap.put(entry.getKey(), (new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".").append(str2).toString());
/*     */     } 
/* 249 */     return replacementMap;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\PackageNameCompressor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */