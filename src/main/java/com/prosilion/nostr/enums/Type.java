package com.prosilion.nostr.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
  UPVOTE(Kind.BADGE_AWARD_EVENT, "upvote"),
  DOWNVOTE(Kind.BADGE_AWARD_EVENT, "downvote"),
  REPUTATION(Kind.BADGE_AWARD_EVENT, "reputation");

  private final Kind kind;

  @JsonValue
  private final String name;
}
