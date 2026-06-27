package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.user.PublicKey;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

@Slf4j
public class SetsPairedEvents<T extends SetsPairedEventTagIF> extends ImmutablePair<BadgeDefinitionGenericEventAux, T> {
  private final TupleATagETag tupleATagETag;

  public SetsPairedEvents(
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
  public final EventTag getEventTag() {
    return tupleATagETag.getRight();
  }

  @JsonIgnore
  public final String getAwardEventId() {
    return getEventTag().getIdEvent();
  }

  @JsonIgnore
  public final Optional<Relay> getAwardEventRelay() {
    return getEventTag().findRelay();
  }

  @JsonIgnore
  public final AddressTag getAddressTag() {
    return tupleATagETag.getLeft();
  }

  @JsonIgnore
  public final Kind getDefinitionEventKind() {
    return getAddressTag().getKind();
  }

  @JsonIgnore
  public final PublicKey getDefinitionEventPublicKey() {
    return getAddressTag().getPublicKey();
  }

  @JsonIgnore
  public final IdentifierTag getDefinitionEventIdentifierTag() {
    return getAddressTag().getIdentifierTag();
  }

  @JsonIgnore
  public final Optional<Relay> getDefinitionEventRelay() {
    return getAddressTag().findRelay();
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return getRight().getPublicKey();
  }

  private static class TupleATagETag extends ImmutablePair<AddressTag, EventTag> {
    public TupleATagETag(@NonNull AddressTag left, @NonNull EventTag right) {
      super(left, right);
    }
  }
}
