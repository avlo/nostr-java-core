package com.prosilion.nostr;

import lombok.experimental.StandardException;
import lombok.NonNull;

@StandardException
public class NostrException extends RuntimeException {
  public NostrException(String message) {
    super(message);
  }

  public static void testBoolean(boolean value, @NonNull String message) {
    if (!value) throw new NostrException(message);
  }
}
