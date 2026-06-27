package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.user.PublicKey;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

@Getter
@Slf4j
public class TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<T extends SetsEventTupleNeedsAppropriateNameIF> extends ImmutablePair<BadgeDefinitionGenericEventAux, T> {
  private final TupleATagETag tupleATagETag;

  public TupleDefnEventAuxAwardEventAuxNeedsAppropriateName(
     @NonNull BadgeDefinitionGenericEventAux badgeDefinition,
     @NonNull T badgeAward) {
    super(badgeDefinition, badgeAward);

    this.tupleATagETag = new TupleATagETag(
       new AddressTag(
          badgeDefinition.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getKind(),
          badgeDefinition.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getPublicKey(),
          badgeDefinition.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getIdentifierTag(),
          badgeDefinition.getRelay().orElse(null)),
       new EventTag(
          badgeAward.getIdEvent(),
          badgeAward.findRelay().map(Relay::getUrl).orElse(null)));
  }

  @JsonIgnore
  public final String getAwardEventId() {
    return tupleATagETag.getRight().getIdEvent();
  }

  @JsonIgnore
  public final Optional<Relay> getAwardEventRelay() {
    return tupleATagETag.getRight().findRelay();
  }

  @JsonIgnore
  public final AddressTag getDefinitionEventAsAddressTag() {
    return tupleATagETag.getLeft();
  }

  @JsonIgnore
  public final Optional<Relay> getDefinitionEventRelay() {
    return tupleATagETag.getLeft().findRelay();
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return getRight().getPublicKey();
  }

  public static class TupleATagETag extends ImmutablePair<AddressTag, EventTag> {
    public TupleATagETag(@NonNull AddressTag left, @NonNull EventTag right) {
      super(left, right);
    }
  }
}
