package org.apache.commons.text.similarity;

public interface EditDistance<R> extends SimilarityScore<R> {
  R apply(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\similarity\EditDistance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */