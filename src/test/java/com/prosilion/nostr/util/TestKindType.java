package com.prosilion.nostr.util;

import com.fasterxml.jackson.annotation.JsonValue;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.KindTypeIF;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestKindType implements KindTypeIF {
  UPVOTE(Kind.BADGE_AWARD_EVENT, Kind.BADGE_DEFINITION_EVENT, "upvote"),
  DOWNVOTE(Kind.BADGE_AWARD_EVENT, Kind.BADGE_DEFINITION_EVENT, "downvote"),
  REPUTATION(Kind.BADGE_AWARD_EVENT, Kind.BADGE_DEFINITION_EVENT, "reputation");

  private final Kind kind;
  private final Kind kindDefinition;

  @JsonValue
  private final String name;


  @Override
  public KindTypeIF[] getValues() {
    return TestKindType.values();
  }

  @Generated
  public Kind getKind() {
    return this.kind;
  }

  @Generated
  public Kind getKindDefinition() {
    return this.kindDefinition;
  }

  @Generated
  public String getName() {
    return this.name;
  }
}
