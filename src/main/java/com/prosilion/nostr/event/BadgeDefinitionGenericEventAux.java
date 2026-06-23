package com.prosilion.nostr.event;

import com.prosilion.nostr.tag.RelayTag;
import java.util.Optional;
import lombok.NonNull;

public class BadgeDefinitionGenericEventAux extends BadgeDefinitionGenericEvent {
  private final RelayTag relayTag;

  public BadgeDefinitionGenericEventAux(
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     RelayTag relayTag) {
    super(badgeDefinitionGenericEvent.asGenericEventRecord());
    this.relayTag = relayTag;
  }

  @Override
//  TODO: potentially removable, since exists in EventIF
  public Optional<RelayTag> getRelayTag() {
    return Optional.ofNullable(relayTag);
  }
}
