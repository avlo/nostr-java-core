package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.event.internal.Relay;
import lombok.NonNull;

public class BadgeAwardGenericEventAux extends BadgeGenericEventAux<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> {
  public BadgeAwardGenericEventAux(
     @NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent, Relay relay) {
    super(badgeAwardGenericEvent, relay);
  }

  @JsonIgnore
  public final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> getBadgeAwardGenericEvent() {
    return getEvent();
  }
}
