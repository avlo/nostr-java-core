package com.prosilion.nostr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
  AUTH("AUTH"),
  EVENT("EVENT"),
  REQ("REQ"),
  CLOSE("CLOSE"),
  CLOSED("CLOSED"),
  NOTICE("NOTICE"),
  EOSE("EOSE"),
  OK("OK");

  public final String name;
}
