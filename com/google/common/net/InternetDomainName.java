/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
/*     */ import com.google.thirdparty.publicsuffix.PublicSuffixType;
/*     */ import java.util.List;
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
/*     */ @Immutable
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class InternetDomainName
/*     */ {
/*  79 */   private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
/*  80 */   private static final Splitter DOT_SPLITTER = Splitter.on('.');
/*  81 */   private static final Joiner DOT_JOINER = Joiner.on('.');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int NO_SUFFIX_FOUND = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_PARTS = 127;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_LENGTH = 253;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_DOMAIN_PART_LENGTH = 63;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableList<String> parts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int publicSuffixIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int registrySuffixIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InternetDomainName(String name) {
/* 138 */     name = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, '.'));
/*     */     
/* 140 */     if (name.endsWith(".")) {
/* 141 */       name = name.substring(0, name.length() - 1);
/*     */     }
/*     */     
/* 144 */     Preconditions.checkArgument((name.length() <= 253), "Domain name too long: '%s':", name);
/* 145 */     this.name = name;
/*     */     
/* 147 */     this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(name));
/* 148 */     Preconditions.checkArgument((this.parts.size() <= 127), "Domain has too many parts: '%s'", name);
/* 149 */     Preconditions.checkArgument(validateSyntax((List<String>)this.parts), "Not a valid domain name: '%s'", name);
/*     */     
/* 151 */     this.publicSuffixIndex = findSuffixOfType(Optional.absent());
/* 152 */     this.registrySuffixIndex = findSuffixOfType(Optional.of(PublicSuffixType.REGISTRY));
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
/*     */   private int findSuffixOfType(Optional<PublicSuffixType> desiredType) {
/* 165 */     int partsSize = this.parts.size();
/*     */     
/* 167 */     for (int i = 0; i < partsSize; i++) {
/* 168 */       String ancestorName = DOT_JOINER.join((Iterable)this.parts.subList(i, partsSize));
/*     */       
/* 170 */       if (matchesType(desiredType, 
/* 171 */           Optional.fromNullable(PublicSuffixPatterns.EXACT.get(ancestorName)))) {
/* 172 */         return i;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 178 */       if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
/* 179 */         return i + 1;
/*     */       }
/*     */       
/* 182 */       if (matchesWildcardSuffixType(desiredType, ancestorName)) {
/* 183 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     return -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InternetDomainName from(String domain) {
/* 210 */     return new InternetDomainName((String)Preconditions.checkNotNull(domain));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateSyntax(List<String> parts) {
/* 220 */     int lastIndex = parts.size() - 1;
/*     */ 
/*     */ 
/*     */     
/* 224 */     if (!validatePart(parts.get(lastIndex), true)) {
/* 225 */       return false;
/*     */     }
/*     */     
/* 228 */     for (int i = 0; i < lastIndex; i++) {
/* 229 */       String part = parts.get(i);
/* 230 */       if (!validatePart(part, false)) {
/* 231 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 235 */     return true;
/*     */   }
/*     */   
/* 238 */   private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
/*     */   
/* 240 */   private static final CharMatcher DIGIT_MATCHER = CharMatcher.inRange('0', '9');
/*     */ 
/*     */   
/* 243 */   private static final CharMatcher LETTER_MATCHER = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z'));
/*     */   
/* 245 */   private static final CharMatcher PART_CHAR_MATCHER = DIGIT_MATCHER
/* 246 */     .or(LETTER_MATCHER).or(DASH_MATCHER);
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
/*     */   private static boolean validatePart(String part, boolean isFinalPart) {
/* 261 */     if (part.length() < 1 || part.length() > 63) {
/* 262 */       return false;
/*     */     }
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
/* 275 */     String asciiChars = CharMatcher.ascii().retainFrom(part);
/*     */     
/* 277 */     if (!PART_CHAR_MATCHER.matchesAllOf(asciiChars)) {
/* 278 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 283 */     if (DASH_MATCHER.matches(part.charAt(0)) || DASH_MATCHER
/* 284 */       .matches(part.charAt(part.length() - 1))) {
/* 285 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     if (isFinalPart && DIGIT_MATCHER.matches(part.charAt(0))) {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<String> parts() {
/* 308 */     return this.parts;
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
/*     */   public boolean isPublicSuffix() {
/* 332 */     return (this.publicSuffixIndex == 0);
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
/*     */ 
/*     */   
/*     */   public boolean hasPublicSuffix() {
/* 348 */     return (this.publicSuffixIndex != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName publicSuffix() {
/* 358 */     return hasPublicSuffix() ? ancestor(this.publicSuffixIndex) : null;
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
/*     */ 
/*     */   
/*     */   public boolean isUnderPublicSuffix() {
/* 374 */     return (this.publicSuffixIndex > 0);
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
/*     */ 
/*     */   
/*     */   public boolean isTopPrivateDomain() {
/* 390 */     return (this.publicSuffixIndex == 1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName topPrivateDomain() {
/* 410 */     if (isTopPrivateDomain()) {
/* 411 */       return this;
/*     */     }
/* 413 */     Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
/* 414 */     return ancestor(this.publicSuffixIndex - 1);
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
/*     */   public boolean isRegistrySuffix() {
/* 441 */     return (this.registrySuffixIndex == 0);
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
/*     */   
/*     */   public boolean hasRegistrySuffix() {
/* 456 */     return (this.registrySuffixIndex != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName registrySuffix() {
/* 466 */     return hasRegistrySuffix() ? ancestor(this.registrySuffixIndex) : null;
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
/*     */   public boolean isUnderRegistrySuffix() {
/* 478 */     return (this.registrySuffixIndex > 0);
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
/*     */   
/*     */   public boolean isTopDomainUnderRegistrySuffix() {
/* 493 */     return (this.registrySuffixIndex == 1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName topDomainUnderRegistrySuffix() {
/* 512 */     if (isTopDomainUnderRegistrySuffix()) {
/* 513 */       return this;
/*     */     }
/* 515 */     Preconditions.checkState(isUnderRegistrySuffix(), "Not under a registry suffix: %s", this.name);
/* 516 */     return ancestor(this.registrySuffixIndex - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasParent() {
/* 521 */     return (this.parts.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName parent() {
/* 532 */     Preconditions.checkState(hasParent(), "Domain '%s' has no parent", this.name);
/* 533 */     return ancestor(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InternetDomainName ancestor(int levels) {
/* 544 */     return from(DOT_JOINER.join((Iterable)this.parts.subList(levels, this.parts.size())));
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
/*     */   public InternetDomainName child(String leftParts) {
/* 557 */     String str1 = (String)Preconditions.checkNotNull(leftParts), str2 = this.name; return from((new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".").append(str2).toString());
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
/*     */   public static boolean isValid(String name) {
/*     */     try {
/* 585 */       from(name);
/* 586 */       return true;
/* 587 */     } catch (IllegalArgumentException e) {
/* 588 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchesWildcardSuffixType(Optional<PublicSuffixType> desiredType, String domain) {
/* 598 */     List<String> pieces = DOT_SPLITTER.limit(2).splitToList(domain);
/* 599 */     return (pieces.size() == 2 && 
/* 600 */       matchesType(desiredType, 
/* 601 */         Optional.fromNullable(PublicSuffixPatterns.UNDER.get(pieces.get(1)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchesType(Optional<PublicSuffixType> desiredType, Optional<PublicSuffixType> actualType) {
/* 610 */     return desiredType.isPresent() ? desiredType.equals(actualType) : actualType.isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 616 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 626 */     if (object == this) {
/* 627 */       return true;
/*     */     }
/*     */     
/* 630 */     if (object instanceof InternetDomainName) {
/* 631 */       InternetDomainName that = (InternetDomainName)object;
/* 632 */       return this.name.equals(that.name);
/*     */     } 
/*     */     
/* 635 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 640 */     return this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\net\InternetDomainName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */