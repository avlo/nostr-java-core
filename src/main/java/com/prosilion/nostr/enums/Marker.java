
package com.prosilion.nostr.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.NonNull;

public enum Marker {
	ROOT("root"),
  REPLY("reply"),
  MENTION("mention"),
  FORK("fork"),
  CREATED("created"),
  DESTROYED("destroyed"),
  REDEEMED("redeemed");

  private final String value;

  Marker(@NonNull String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
