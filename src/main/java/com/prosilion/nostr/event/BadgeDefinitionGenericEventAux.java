package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.event.internal.Relay;
import lombok.NonNull;

public class BadgeDefinitionGenericEventAux extends BadgeGenericEventAux<BadgeDefinitionGenericEvent> {
  public BadgeDefinitionGenericEventAux(
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     Relay relay) {
    super(badgeDefinitionGenericEvent, relay);
  }

  @JsonIgnore
  public final BadgeDefinitionGenericEvent getBadgeDefinitionGenericEvent() {
    return getEvent();
  }
}
