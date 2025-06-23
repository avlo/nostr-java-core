package com.prosilion.nostr.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum KindType implements KindTypeIF {
//  no default values in nostr-java-core.  to be defined in clients/callers implementing KindTypeIF
  ;

  private final Kind kind;
  private final Kind kindDefinition;

  @JsonValue
  private final String name;

  KindType(Kind kind, Kind kindDefinition, String name) {
    this.kind = kind;
    this.kindDefinition = kindDefinition;
    this.name = name;
  }

  @Override
  public KindTypeIF[] getValues() {
    return KindType.values();
  }
}
