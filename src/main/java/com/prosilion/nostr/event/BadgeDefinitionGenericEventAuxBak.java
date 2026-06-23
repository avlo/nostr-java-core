//package com.prosilion.nostr.event;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.prosilion.nostr.tag.RelayTag;
//import java.util.Optional;
//import lombok.NonNull;
//
//public class BadgeDefinitionGenericEventAux extends AddressableEvent {
//  private final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;
//  private final RelayTag relayTag;
//
//  public BadgeDefinitionGenericEventAux(
//     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
//     @NonNull RelayTag relayTag) {
//    super(badgeDefinitionGenericEvent.asGenericEventRecord());
//    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
//    this.relayTag = relayTag;
//  }
//
//  public BadgeDefinitionGenericEventAux(
//     @NonNull GenericEventRecord genericEventRecord,
//     @NonNull RelayTag relayTag) {
//    super(genericEventRecord);
//    this.relayTag = relayTag;
//  }
//
//  @JsonIgnore
//  @Override
//  public Optional<RelayTag> getRelayTag() {
//    return Optional.ofNullable(relayTag);
//  }
//}
