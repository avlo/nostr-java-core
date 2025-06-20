package com.prosilion.nostr.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KindType implements KindTypeIF {
//  no default values in nostr-java-core.  to be defined in clients/callers implementing KindTypeIF
  ;

  private final Kind kind;

  @JsonValue
  private final String name;
}
