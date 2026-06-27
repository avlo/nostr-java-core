package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @JsonIgnore
  public final Optional<Relay> getRelay() {
    return Optional.ofNullable(relay);
  }

  @Override
  @JsonIgnore
  public String getIdEvent() {
    return getBadgeAwardGenericEvent().getId();
  }

  @Override
  @JsonIgnore
  public Optional<Relay> findRelay() {
    return Optional.ofNullable(relay);
  }

  @Override
  @JsonIgnore
  public PublicKey getPublicKey() {
    return getBadgeAwardGenericEvent().getPublicKey();
  }
}
