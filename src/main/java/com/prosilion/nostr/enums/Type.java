package com.prosilion.nostr.enums;

import lombok.Getter;

@Getter
public enum Type {
  UPVOTE("upvote"),
  DOWNVOTE("downvote"),
  REPUTATION("reputation");

  private final String name;

  Type(String name) {
    this.name = name;
  }
}
