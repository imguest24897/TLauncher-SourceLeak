package org.apache.commons.text.diff;

public interface CommandVisitor<T> {
  void visitDeleteCommand(T paramT);
  
  void visitInsertCommand(T paramT);
  
  void visitKeepCommand(T paramT);
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\diff\CommandVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */