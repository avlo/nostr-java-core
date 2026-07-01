package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.user.PublicKey;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public class SetsPairedEvents {
  private final PublicKey recipientPublicKey;
  private final ATagETagPair aTagETagPair;

  public SetsPairedEvents(@NonNull BadgeDefinitionGenericEventAux defnAux, @NonNull BadgeAwardGenericEventAux awardAux) {
    this(
      new AddressTag(
        defnAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getKind(),
        defnAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getPublicKey(),
        defnAux.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getIdentifierTag(),
        defnAux.getRelay().orElse(null)),
      new EventTag(
        awardAux.getEventId(),
        awardAux.findRelay().map(Relay::getUrl).orElse(null)),
      awardAux.getPublicKey());
  }

  public SetsPairedEvents(@NonNull AddressTag addressTag, @NonNull EventTag eventTag, @NonNull PublicKey publicKey) {
    this.recipientPublicKey = publicKey;
    this.aTagETagPair = new ATagETagPair(addressTag, eventTag);
  }

  @JsonIgnore
  public final EventTag getEventTag() {
    return aTagETagPair.getRight();
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
    return aTagETagPair.getLeft();
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
    return recipientPublicKey;
  }

  private static class ATagETagPair extends ImmutablePair<AddressTag, EventTag> implements Comparable<Pair<AddressTag, EventTag>> {
    public ATagETagPair(@NonNull AddressTag left, @NonNull EventTag right) {
      super(left, right);
    }

    @Override
    public final boolean equals(Object that) {
      if (that == null || getClass() != that.getClass()) return false;
      ATagETagPair thatATagETagPair = (ATagETagPair) that;
      return Objects.equals(left, thatATagETagPair.left) &&
        Objects.equals(right, thatATagETagPair.right);
    }
  }

  @Override
  public final boolean equals(Object that) {
    if (that == null || getClass() != that.getClass()) return false;
    SetsPairedEvents thatSetsPairedEvents = (SetsPairedEvents) that;
    return Objects.equals(recipientPublicKey, thatSetsPairedEvents.recipientPublicKey) &&
      Objects.equals(aTagETagPair, thatSetsPairedEvents.aTagETagPair);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(recipientPublicKey, aTagETagPair);
  }
}
