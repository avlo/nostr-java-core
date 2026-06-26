package com.prosilion.nostr.event;

import com.prosilion.nostr.event.internal.Relay;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

// TODO: consolidate common fxnality w/ BadgeDefinitionGenericEventAux
@Getter
public class BadgeAwardGenericEventAux {
  private final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent;
  private final Relay relay;

  public BadgeAwardGenericEventAux(
     @NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent, Relay relay) {
    this.badgeAwardGenericEvent = badgeAwardGenericEvent;
    this.relay = badgeAwardGenericEvent.getRelay().orElse(relay);
  }

  public final Optional<Relay> getRelay() {
    return Optional.ofNullable(relay);
  }
}
