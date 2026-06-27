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
public class SetsPairedEvents<T extends SetsPairedEventTagIF> {
  private final PublicKey recipientPublicKey;
  private final ATagETagPair aTagETagPair;

  public SetsPairedEvents(@NonNull BadgeDefinitionGenericEventAux badgeDefinition, @NonNull T badgeAward) {
    this.recipientPublicKey = badgeAward.getPublicKey();
    this.aTagETagPair = new ATagETagPair(
       new AddressTag(
          badgeDefinition.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getKind(),
          badgeDefinition.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getPublicKey(),
          badgeDefinition.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag().getIdentifierTag(),
          badgeDefinition.getRelay().orElse(null)),
       new EventTag(
          badgeAward.getEventId(),
          badgeAward.findRelay().map(Relay::getUrl).orElse(null)));
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

  private static class ATagETagPair extends ImmutablePair<AddressTag, EventTag> {
    public ATagETagPair(@NonNull AddressTag left, @NonNull EventTag right) {
      super(left, right);
    }
  }
}
