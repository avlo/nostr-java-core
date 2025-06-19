package com.prosilion.nostr.event;

import com.prosilion.nostr.crypto.HexStringValidator;
import lombok.Getter;

public record GenericEventId(@Getter String id) {
  public GenericEventId {
    id = HexStringValidator.validateHex(id, 64);
  }
}
