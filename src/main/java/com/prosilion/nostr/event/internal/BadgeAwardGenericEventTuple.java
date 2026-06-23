//package com.prosilion.nostr.event.internal;
//
//import com.prosilion.nostr.NostrException;
//import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
//import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
//import java.util.Optional;
//import lombok.NonNull;
//import org.apache.commons.lang3.tuple.ImmutablePair;
//
//public class BadgeAwardGenericEventTuple extends {
//
//  public BadgeAwardGenericEventTuple(
//     @NonNull BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardGenericEvent,
//     @NonNull RelayAux eventTagRelayAux) {
//    super(badgeAwardGenericEvent, eventTagRelayAux);
//  }
//
//  public final BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> getBadgeAwardGenericEventAux() {
//    return getLeft();
//  }
//
//  public final Optional<RelayAux> findEventTagRelayAux() {
//    return Optional.ofNullable(getRight());
//  }
//
//  public final RelayAux requireEventTagRelayAux() {
//    return Optional.ofNullable(getRight()).orElseThrow(() ->
//       new NostrException("EventTriple does not contain an EventTag relay"));
//  }
//}
