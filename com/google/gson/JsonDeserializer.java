package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializer<T> {
  T deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException;
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\gson\JsonDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */