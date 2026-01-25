package com.prosilion.nostr;

import lombok.experimental.StandardException;
import org.springframework.lang.NonNull;

@StandardException
public class NostrException extends RuntimeException {
  public NostrException(String message) {
    super(message);
  }

  public static void testBoolean(boolean value, @NonNull String message) {
    if (!value) throw new NostrException(message);
  }
}
