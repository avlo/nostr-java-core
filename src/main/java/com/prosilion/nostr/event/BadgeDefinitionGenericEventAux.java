package com.prosilion.nostr.event;

import com.prosilion.nostr.event.internal.Relay;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

// TODO: consolidate common fxnality w/ BadgeAwardGenericEventAux
@Getter
public class BadgeDefinitionGenericEventAux {
  private final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;
  private final Relay relay;

  public BadgeDefinitionGenericEventAux(
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     Relay relay) {
    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
    this.relay = badgeDefinitionGenericEvent.getRelay().orElse(relay);
  }

  public final Optional<Relay> getRelay() {
    return Optional.ofNullable(relay);
  }
}
