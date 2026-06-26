package com.prosilion.nostr.tag;

import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.util.Util;
import java.util.Optional;
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

    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent();

    Util.debug(log, true, 'A');
    log.debug("logging various relays");

    Util.debug(log, "badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().findRelay():\n  [ {} ]",
       badgeDefinitionGenericEvent.asAddressableEventAddressTag().findRelay().map(Relay::getUrl).orElse("EMPTY RELAY"), true, '1');

    Optional<Relay> addressTagRelay = badgeDefinitionGenericEventAux.getRelay();

    Util.debug(log, "badgeDefinitionGenericEventAux.getRelay():\n  [ {} ]",
       badgeDefinitionGenericEventAux.getRelay().map(Relay::getUrl).orElse("EMPTY OPTIONAL"), true, '2');

    AddressTag addressTagSupplimentalRelay = new AddressTag(
       badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getKind(),
       badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getPublicKey(),
       badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getIdentifierTag(),
       addressTagRelay.orElse(null));

    Optional<RelayTag> awardRelay = badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getRelayTag();

    Util.debug(log, "badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getRelayTag():\n  [ {} ]",
       badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getRelayTag().map(RelayTag::relay).map(Relay::getUrl).orElse("EMPTY"), true, '3');

    Optional<Relay> awardAuxRelayTag = badgeAwardGenericEventAux.getRelay();

    Util.debug(log, "badgeAwardGenericEventAux.getRelay():\n  [ {} ]",
       badgeAwardGenericEventAux.getRelay().map(Relay::getUrl).orElse("EMPTY OPTIONAL"), true, '4');

    Optional<Relay> eventTagRelay = awardRelay.map(RelayTag::getRelay);

    Util.debug(log, true, 'B');

    EventTag eventTagSupplimentalRelay = new EventTag(
       badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getId(),
       eventTagRelay.map(Relay::getUrl).orElse(null));

    this.tupleATagETag = new TupleATagETag(
       addressTagSupplimentalRelay,
       eventTagSupplimentalRelay);
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
}
