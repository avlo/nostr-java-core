package com.prosilion.nostr.event;

import com.prosilion.nostr.tag.RelayTag;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class BadgeDefinitionGenericEventAux {
  private final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;
  private final RelayTag relayTag;

  public BadgeDefinitionGenericEventAux(
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     RelayTag relayTag) {
    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
    this.relayTag = badgeDefinitionGenericEvent.getRelayTag().orElse(relayTag);
  }
}
