package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.crypto.HexStringValidator;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;

public record GenericEventId(@Getter String id) {
  @JsonIgnore
  private static final Log log = LogFactory.getLog(GenericEventId.class);

  public GenericEventId {
    id = validateId(id);
  }

  private String validateId(@NonNull String id) {
    HexStringValidator.validateHex(id, 64);
    return id;
  }
}
