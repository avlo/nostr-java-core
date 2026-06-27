package com.prosilion.nostr.event;

import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.SetsEventTupleNeedsAppropriateNameIF;
import com.prosilion.nostr.user.PublicKey;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

// TODO: consolidate common fxnality w/ BadgeDefinitionGenericEventAux
@Getter
public class BadgeAwardGenericEventAux implements SetsEventTupleNeedsAppropriateNameIF {
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

  @Override
  public String getIdEvent() {
    return getBadgeAwardGenericEvent().getId();
  }

  @Override
  public Optional<Relay> findRelay() {
    return Optional.ofNullable(relay);
  }

  @Override
  public PublicKey getPublicKey() {
    return getBadgeAwardGenericEvent().getPublicKey();
  }
}
