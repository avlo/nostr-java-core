package com.prosilion.nostr.tag;

import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.util.Util;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

@Getter
@Slf4j
public class TupleDefnEventAuxAwardEventAux extends ImmutablePair<BadgeDefinitionGenericEventAux, BadgeAwardGenericEventAux> {
  private final TupleATagETag tupleATagETag;

  public TupleDefnEventAuxAwardEventAux(
     @NonNull BadgeDefinitionGenericEventAux badgeDefinitionGenericEventAux,
     @NonNull BadgeAwardGenericEventAux badgeAwardGenericEventAux) {
    super(badgeDefinitionGenericEventAux, badgeAwardGenericEventAux);

    Util.debug(log, "badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getRelayTag():\n  [ {} ]",
       badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getRelayTag().map(RelayTag::relay).map(Relay::getUrl).orElse("EMPTY RELAY"), true, '1');
    Util.debug(log, "badgeAwardGenericEventAux.getRelay():\n  [ {} ]",
       badgeAwardGenericEventAux.getRelay().map(Relay::getUrl).orElse("EMPTY RELAY"), true, '2');

    Util.debug(log, "badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().getRelayTag():\n  [ {} ]",
       badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl).orElse("EMPTY RELAY"), true, '3');
    Util.debug(log, "badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().findRelay():\n  [ {} ]",
       badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().findRelay().map(Relay::getUrl).orElse("EMPTY RELAY"), true, '3');
    Util.debug(log, "badgeDefinitionGenericEventAux.getRelay():\n  [ {} ]",
       badgeDefinitionGenericEventAux.getRelay().map(Relay::getUrl).orElse("EMPTY RELAY"), true, '4');

    this.tupleATagETag = new TupleATagETag(
       new AddressTag(
          badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getKind(),
          badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getPublicKey(),
          badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getIdentifierTag(),
          badgeDefinitionGenericEventAux.getRelay().orElse(null)),
       new EventTag(
          badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getId(),
          badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl).orElse(null)));
  }

  public final String getAwardEventId() {
    return tupleATagETag.right.getIdEvent();
  }

  public final Relay getAwardEventRelay() {
    return tupleATagETag.right.requireRelay();
  }

  public final AddressTag getDefinitionEventAsAddressTag() {
    return tupleATagETag.left;
  }

  public final Relay getDefinitionEventRelay() {
    return tupleATagETag.left.requireRelay();
  }

  public static class TupleATagETag extends ImmutablePair<AddressTag, EventTag> {
    public TupleATagETag(@NonNull AddressTag left, @NonNull EventTag right) {
      super(left, right);
    }
  }
}
