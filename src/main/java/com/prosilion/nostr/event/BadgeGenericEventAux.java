package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.user.PublicKey;
import java.util.Optional;
import lombok.NonNull;

public abstract class BadgeGenericEventAux<T extends BaseEvent> implements SetsPairedEventTagIF {
  private final T event;
  private final Relay relay;

  protected BadgeGenericEventAux(@NonNull T event, Relay relay) {
    this.event = event;
    this.relay = event.getRelayTag().map(RelayTag::getRelay).orElse(relay);
  }

  @JsonIgnore
  protected final T getEvent() {
    return event;
  }

  @JsonIgnore
  public final Optional<Relay> getRelay() {
    return Optional.ofNullable(relay);
  }

  @Override
  @JsonIgnore
  public final String getEventId() {
    return event.getId();
  }

  @Override
  @JsonIgnore
  public final Optional<Relay> findRelay() {
    return Optional.ofNullable(relay);
  }

  @Override
  @JsonIgnore
  public final PublicKey getPublicKey() {
    return event.getPublicKey();
  }
}
