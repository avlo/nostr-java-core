package com.prosilion.nostr;

import lombok.experimental.StandardException;

@StandardException
public class NostrException extends RuntimeException {
  public NostrException(String message) {
    super(message);
  }
}
