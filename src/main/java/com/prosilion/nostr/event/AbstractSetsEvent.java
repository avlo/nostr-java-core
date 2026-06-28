package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AbstractSetsEvent<T extends SetsPairedEventTagIF> extends AddressableEvent implements TagMappedEventIF {
  public static final String PUBKEYS_MUST_MATCH =
     "AbstractSetsEvent AwardEvent PublicKeys must all match, but instead contained [%s] different keys:\n  [%s]";
  private static final String EMPTY_PAIRS = "AbstractSetsEvent List<SetsPairedEvents> is empty";
  @JsonIgnore
  protected final List<SetsPairedEvents<T>> setsPairedEventsList;

  protected AbstractSetsEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull IdentifierTag identifierTag,
     @NonNull List<SetsPairedEvents<T>> setsPairedEventsList,
     @NonNull List<BaseTag> tags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(identity, kind, identifierTag,
       buildTags(setsPairedEventsList, tags), content, relay);
    this.setsPairedEventsList = setsPairedEventsList;
  }

  protected AbstractSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<SetsPairedEvents<T>> setsPairedEventsList) {
    super(genericEventRecord);
    this.setsPairedEventsList = setsPairedEventsList;
  }

  @JsonIgnore
  public final String getEventId() {
    return super.getId();
  }

  @JsonIgnore
  public final Optional<Relay> findRelay() {
    return super.getRelay();
  }

  @JsonIgnore
  public final List<AddressTag> getAddressTags() {
    return setsPairedEventsList.stream()
       .map(SetsPairedEvents::getAddressTag).toList();
  }

  @JsonIgnore
  public final List<EventTag> getEventTags() {
    return setsPairedEventsList.stream()
       .map(SetsPairedEvents::getEventTag)
       .toList();
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return setsPairedEventsList.getFirst().getAwardRecipientPublicKey();
  }

  protected static <T extends SetsPairedEventTagIF> List<BaseTag> buildTags(
     @NonNull List<SetsPairedEvents<T>> setsPairedEventsList,
     @NonNull List<BaseTag> baseTags) {
    return Stream.concat(
       flattenSetsPairedEventsToTags(
          validateIdenticalBadgeAwardGenericEventsPublicKeys(setsPairedEventsList)),
       baseTags.stream()
          .filter(Predicate.not(EventTag.class::isInstance))
          .filter(Predicate.not(AddressTag.class::isInstance))).toList();
  }

  private static <T extends SetsPairedEventTagIF> Stream<SetsPairedEvents<T>> validateIdenticalBadgeAwardGenericEventsPublicKeys(
     @NonNull List<SetsPairedEvents<T>> pairs) {
    List<PublicKey> uniqueKeys =
       TagMappedEventIF.throwIfEmpty(pairs, EMPTY_PAIRS)
          .map(SetsPairedEvents::getAwardRecipientPublicKey)
          .distinct().toList();
    if (uniqueKeys.size() != 1)
      throw new NostrException(
         String.format(
            PUBKEYS_MUST_MATCH,
            uniqueKeys.size(),
            uniqueKeys.stream().map(PublicKey::toHexString).collect(Collectors.joining("],\n  ["))));
    return pairs.stream();
  }

  private static <T extends SetsPairedEventTagIF> Stream<BaseTag> flattenSetsPairedEventsToTags(
     @NonNull Stream<SetsPairedEvents<T>> setsPairedEventsList) {
    return setsPairedEventsList
       .flatMap(pair ->
          Stream.of(
             pair.getAddressTag(), (BaseTag) pair.getEventTag()));
  }
}
