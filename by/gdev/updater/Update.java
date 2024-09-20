/*     */ package by.gdev.updater;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Update {
/*     */   public void setDescription(Map<String, String> description) {
/*  13 */     this.description = description; } public void setOffers(List<Offer> offers) { this.offers = offers; } public void setSelectedOffer(Offer selectedOffer) { this.selectedOffer = selectedOffer; } public void setMandatory(boolean mandatory) { this.mandatory = mandatory; } public void setAboveMandatoryVersion(Double aboveMandatoryVersion) { this.aboveMandatoryVersion = aboveMandatoryVersion; } public void setMandatoryUpdatedVersions(Set<Double> mandatoryUpdatedVersions) { this.mandatoryUpdatedVersions = mandatoryUpdatedVersions; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Update)) return false;  Update other = (Update)o; if (!other.canEqual(this)) return false;  Object<String, String> this$description = (Object<String, String>)getDescription(), other$description = (Object<String, String>)other.getDescription(); if ((this$description == null) ? (other$description != null) : !this$description.equals(other$description)) return false;  Object<Offer> this$offers = (Object<Offer>)getOffers(), other$offers = (Object<Offer>)other.getOffers(); if ((this$offers == null) ? (other$offers != null) : !this$offers.equals(other$offers)) return false;  if (getOfferEmptyCheckboxDelay() != other.getOfferEmptyCheckboxDelay()) return false;  if (getOfferDelay() != other.getOfferDelay()) return false;  if (Double.compare(getVersion(), other.getVersion()) != 0) return false;  Object<String> this$rootAccessExe = (Object<String>)getRootAccessExe(), other$rootAccessExe = (Object<String>)other.getRootAccessExe(); if ((this$rootAccessExe == null) ? (other$rootAccessExe != null) : !this$rootAccessExe.equals(other$rootAccessExe)) return false;  if (isUpdaterLaterInstall() != other.isUpdaterLaterInstall()) return false;  Object this$selectedOffer = getSelectedOffer(), other$selectedOffer = other.getSelectedOffer(); if ((this$selectedOffer == null) ? (other$selectedOffer != null) : !this$selectedOffer.equals(other$selectedOffer)) return false;  if (getUpdaterView() != other.getUpdaterView()) return false;  Object<String, List<Banner>> this$banners = (Object<String, List<Banner>>)getBanners(), other$banners = (Object<String, List<Banner>>)other.getBanners(); if ((this$banners == null) ? (other$banners != null) : !this$banners.equals(other$banners)) return false;  if (isMandatory() != other.isMandatory()) return false;  Object this$aboveMandatoryVersion = getAboveMandatoryVersion(), other$aboveMandatoryVersion = other.getAboveMandatoryVersion(); if ((this$aboveMandatoryVersion == null) ? (other$aboveMandatoryVersion != null) : !this$aboveMandatoryVersion.equals(other$aboveMandatoryVersion)) return false;  Object<Double> this$mandatoryUpdatedVersions = (Object<Double>)getMandatoryUpdatedVersions(), other$mandatoryUpdatedVersions = (Object<Double>)other.getMandatoryUpdatedVersions(); return !((this$mandatoryUpdatedVersions == null) ? (other$mandatoryUpdatedVersions != null) : !this$mandatoryUpdatedVersions.equals(other$mandatoryUpdatedVersions)); } protected boolean canEqual(Object other) { return other instanceof Update; } public int hashCode() { int PRIME = 59; result = 1; Object<String, String> $description = (Object<String, String>)getDescription(); result = result * 59 + (($description == null) ? 43 : $description.hashCode()); Object<Offer> $offers = (Object<Offer>)getOffers(); result = result * 59 + (($offers == null) ? 43 : $offers.hashCode()); result = result * 59 + getOfferEmptyCheckboxDelay(); result = result * 59 + getOfferDelay(); long $version = Double.doubleToLongBits(getVersion()); result = result * 59 + (int)($version >>> 32L ^ $version); Object<String> $rootAccessExe = (Object<String>)getRootAccessExe(); result = result * 59 + (($rootAccessExe == null) ? 43 : $rootAccessExe.hashCode()); result = result * 59 + (isUpdaterLaterInstall() ? 79 : 97); Object $selectedOffer = getSelectedOffer(); result = result * 59 + (($selectedOffer == null) ? 43 : $selectedOffer.hashCode()); result = result * 59 + getUpdaterView(); Object<String, List<Banner>> $banners = (Object<String, List<Banner>>)getBanners(); result = result * 59 + (($banners == null) ? 43 : $banners.hashCode()); result = result * 59 + (isMandatory() ? 79 : 97); Object $aboveMandatoryVersion = getAboveMandatoryVersion(); result = result * 59 + (($aboveMandatoryVersion == null) ? 43 : $aboveMandatoryVersion.hashCode()); Object<Double> $mandatoryUpdatedVersions = (Object<Double>)getMandatoryUpdatedVersions(); return result * 59 + (($mandatoryUpdatedVersions == null) ? 43 : $mandatoryUpdatedVersions.hashCode()); } public String toString() { return "Update(description=" + getDescription() + ", offers=" + getOffers() + ", offerEmptyCheckboxDelay=" + getOfferEmptyCheckboxDelay() + ", offerDelay=" + getOfferDelay() + ", version=" + getVersion() + ", rootAccessExe=" + getRootAccessExe() + ", updaterLaterInstall=" + isUpdaterLaterInstall() + ", selectedOffer=" + getSelectedOffer() + ", updaterView=" + getUpdaterView() + ", banners=" + getBanners() + ", mandatory=" + isMandatory() + ", aboveMandatoryVersion=" + getAboveMandatoryVersion() + ", mandatoryUpdatedVersions=" + getMandatoryUpdatedVersions() + ")"; }
/*     */   
/*     */   private List<Offer> offers; private int offerEmptyCheckboxDelay; private int offerDelay; private double version; private List<String> rootAccessExe; private boolean updaterLaterInstall;
/*  16 */   protected Map<String, String> description = new HashMap<>(); private Offer selectedOffer; private int updaterView; private Map<String, List<Banner>> banners; protected boolean mandatory; private Double aboveMandatoryVersion; public Map<String, String> getDescription() { return this.description; } public List<Offer> getOffers() {
/*  17 */     return this.offers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Offer getSelectedOffer() {
/*  23 */     return this.selectedOffer;
/*     */   }
/*     */   
/*  26 */   public boolean isMandatory() { return this.mandatory; } public Double getAboveMandatoryVersion() {
/*  27 */     return this.aboveMandatoryVersion;
/*  28 */   } private Set<Double> mandatoryUpdatedVersions = new HashSet<>(); public Set<Double> getMandatoryUpdatedVersions() { return this.mandatoryUpdatedVersions; }
/*     */   
/*     */   public boolean isMandatory(String appLocalConfig) {
/*  31 */     if (this.mandatory)
/*  32 */       return true; 
/*  33 */     Double v1 = Double.valueOf(appLocalConfig);
/*  34 */     if (Objects.nonNull(this.aboveMandatoryVersion) && this.aboveMandatoryVersion.compareTo(v1) > 0) {
/*  35 */       return true;
/*     */     }
/*  37 */     return (Objects.nonNull(this.mandatoryUpdatedVersions) && this.mandatoryUpdatedVersions.contains(v1));
/*     */   }
/*     */   
/*     */   public double getVersion() {
/*  41 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(double version) {
/*  45 */     this.version = version;
/*     */   }
/*     */   
/*     */   public Optional<Offer> getOfferByLang(String lang) {
/*  49 */     return this.offers.stream().filter(e -> e.getTopText().containsKey(lang)).findAny();
/*     */   }
/*     */   
/*     */   public String getDescription(String key) {
/*  53 */     return (this.description == null) ? null : this.description.get(key);
/*     */   }
/*     */   
/*     */   public class SearchSucceeded extends SearchResult {
/*     */     public SearchSucceeded(Update response) {
/*  58 */       super((Update)Update.requireNotNull((T)response, "response"));
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<String, List<Banner>> getBanners() {
/*  63 */     return this.banners;
/*     */   }
/*     */   
/*     */   public void setBanners(Map<String, List<Banner>> banners) {
/*  67 */     this.banners = banners;
/*     */   }
/*     */   
/*     */   public int getUpdaterView() {
/*  71 */     return this.updaterView;
/*     */   }
/*     */   
/*     */   public void setUpdaterView(int updaterView) {
/*  75 */     this.updaterView = updaterView;
/*     */   }
/*     */   
/*     */   public abstract class SearchResult {
/*     */     protected final Update response;
/*     */     
/*     */     public SearchResult(Update response) {
/*  82 */       this.response = response;
/*     */     }
/*     */     
/*     */     public final Update getResponse() {
/*  86 */       return this.response;
/*     */     }
/*     */     
/*     */     public final Update getUpdater() {
/*  90 */       return Update.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  95 */       return getClass().getSimpleName() + "{response=" + this.response + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> T requireNotNull(T obj, String name) {
/* 100 */     if (obj == null)
/* 101 */       throw new NullPointerException(name); 
/* 102 */     return obj;
/*     */   }
/*     */   
/*     */   public int getOfferEmptyCheckboxDelay() {
/* 106 */     return this.offerEmptyCheckboxDelay;
/*     */   }
/*     */   
/*     */   public void setOfferEmptyCheckboxDelay(int offerEmptyCheckboxDelay) {
/* 110 */     this.offerEmptyCheckboxDelay = offerEmptyCheckboxDelay;
/*     */   }
/*     */   
/*     */   public int getOfferDelay() {
/* 114 */     return this.offerDelay;
/*     */   }
/*     */   
/*     */   public void setOfferDelay(int offerDelay) {
/* 118 */     this.offerDelay = offerDelay;
/*     */   }
/*     */   
/*     */   public List<String> getRootAccessExe() {
/* 122 */     return this.rootAccessExe;
/*     */   }
/*     */   
/*     */   public void setRootAccessExe(List<String> rootAccessExe) {
/* 126 */     this.rootAccessExe = rootAccessExe;
/*     */   }
/*     */   
/*     */   public boolean isUpdaterLaterInstall() {
/* 130 */     return this.updaterLaterInstall;
/*     */   }
/*     */   
/*     */   public void setUpdaterLaterInstall(boolean updaterLaterInstall) {
/* 134 */     this.updaterLaterInstall = updaterLaterInstall;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\Update.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */