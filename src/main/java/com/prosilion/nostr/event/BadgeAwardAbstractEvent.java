package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public abstract class BadgeAwardAbstractEvent<T extends AddressableEvent> extends UniqueAddressTagEvent implements TagMappedEventIF {
  @JsonIgnore
  private final T addressableEvent;

  public BadgeAwardAbstractEvent(
      @NonNull Identity identity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull Relay relay,
      @NonNull T badgeDefinitionGenericEvent,
      @NonNull String content) throws NostrException {
    this(identity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, List.of(), content);
  }

  public BadgeAwardAbstractEvent(
      @NonNull Identity identity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull Relay relay,
      @NonNull T badgeDefinitionGenericEvent,
      @NonNull List<BaseTag> tags,
      @NonNull String content) throws NostrException {
    this(identity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, tags.stream(), content);
  }

  public BadgeAwardAbstractEvent(
      @NonNull Identity identity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull Relay relay,
      @NonNull T badgeDefinitionGenericEvent,
      @NonNull Stream<BaseTag> tags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.BADGE_AWARD_EVENT,
        badgeDefinitionGenericEvent.asAddressTag(),
        Stream.concat(
                Stream.concat(
                    Stream.of(new PubKeyTag(awardRecipientPublicKey)),
                    Stream.of(new RelayTag(relay))),
                tags
                    .filter(Predicate.not(AddressTag.class::isInstance))
                    .filter(Predicate.not(PubKeyTag.class::isInstance))
                    .filter(Predicate.not(RelayTag.class::isInstance)))
            .toList(),
        content);
    this.addressableEvent = badgeDefinitionGenericEvent;
  }

  public BadgeAwardAbstractEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, T> fxn) {
    super(genericEventRecord);
    this.addressableEvent = mapTagsToEvents(this, fxn, AddressTag.class).getFirst();
  }

  @Override
  @JsonIgnore
  public List<AddressTag> getContainedAddressableEvents() {
    return List.of(addressableEvent.asAddressTag());
  }

  @Override
  public Relay getRelayTagRelay() {
    return getTypeSpecificTags(RelayTag.class).getFirst().getRelay();
  }
  
  public abstract T getBadgeDefinitionGenericEvent();
}
