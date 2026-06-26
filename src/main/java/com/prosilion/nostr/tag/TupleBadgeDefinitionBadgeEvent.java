package com.prosilion.nostr.tag;

import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.internal.Relay;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;

@Getter
public class TupleBadgeDefinitionBadgeEvent extends ImmutablePair<BadgeDefinitionGenericEventAux, BadgeAwardGenericEventAux> {
  private final TupleATagETag tupleATagETag;

  public TupleBadgeDefinitionBadgeEvent(
     @NonNull BadgeDefinitionGenericEventAux badgeDefinitionGenericEventAux,
     @NonNull BadgeAwardGenericEventAux badgeAwardGenericEventAux) {
    super(badgeDefinitionGenericEventAux, badgeAwardGenericEventAux);

    Optional<Relay> definitionRelay = badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().findRelay();

    RelayTag defnAuxRelayTag = badgeDefinitionGenericEventAux.getRelayTag();

    Relay addressTagRelay = definitionRelay.orElse(defnAuxRelayTag.getRelay());

    AddressTag addressTagSupplimentalRelay = new AddressTag(
       badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getKind(),
       badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getPublicKey(),
       badgeDefinitionGenericEventAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getIdentifierTag(),
       addressTagRelay);


    Optional<RelayTag> awardRelay = badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getRelayTag();

    Relay awardAuxRelayTag = badgeAwardGenericEventAux.getRelay();

    Relay eventTagRelay = awardRelay.map(RelayTag::getRelay).orElse(awardAuxRelayTag);

    EventTag eventTagSupplimentalRelay = new EventTag(
       badgeAwardGenericEventAux.getBadgeAwardGenericEvent().getId(),
       eventTagRelay.getUrl());

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
