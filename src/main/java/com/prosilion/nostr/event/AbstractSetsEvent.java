package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSetsEvent extends AddressableEvent implements TagMappedEventIF {
  public static final String PUBKEYS_MUST_MATCH =
    "AbstractSetsEvent AwardEvent PublicKeys must all match, but instead contained [%s] different keys:\n  [%s]";
  private static final String EMPTY_PAIRS = "AbstractSetsEvent List<SetsPairedEvents> is empty";

  @Getter
  @JsonIgnore
  protected final List<SetsPairedEvents> setsPairedEventsList;

  protected AbstractSetsEvent(
    @NonNull Identity identity,
    @NonNull Kind kind,
    @NonNull IdentifierTag identifierTag,
    @NonNull List<SetsPairedEvents> setsPairedEventsList,
    @NonNull List<BaseTag> tags,
    @NonNull String content,
    @NonNull Relay relay) throws NostrException {
    super(identity, kind, identifierTag,
      buildTags(
        cullMatchingSetsPairs(setsPairedEventsList), tags), content, relay);
    this.setsPairedEventsList = cullMatchingSetsPairs(setsPairedEventsList);
  }

  protected AbstractSetsEvent(
    @NonNull GenericEventRecord genericEventRecord,
    @NonNull List<SetsPairedEvents> setsPairedEventsList) {
    super(genericEventRecord);
    this.setsPairedEventsList = cullMatchingSetsPairs(setsPairedEventsList);
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

  protected static List<BaseTag> buildTags(
    @NonNull List<SetsPairedEvents> setsPairedEventsList,
    @NonNull List<BaseTag> baseTags) {
    return Stream.concat(
      setsPairsToBaseTags(
        throwNonUniquePublicKeys(setsPairedEventsList).toList()),
      baseTags.stream()
        .filter(Predicate.not(EventTag.class::isInstance))
        .filter(Predicate.not(AddressTag.class::isInstance))).toList();
  }

  private static Stream<SetsPairedEvents> throwNonUniquePublicKeys(
    @NonNull List<SetsPairedEvents> pairs) {
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

  private static Stream<BaseTag> setsPairsToBaseTags(@NonNull List<SetsPairedEvents> sets) {
    return cullMatchingSetsPairs(sets).stream()
      .flatMap(pair ->
        Stream.of(
          pair.getAddressTag(), (BaseTag) pair.getEventTag()));
  }

  private static List<SetsPairedEvents> cullMatchingSetsPairs(List<SetsPairedEvents> setsPairedEventsList) {
    List<SetsPairedEvents> distinctSets = setsPairedEventsList.stream().distinct().toList();
    int initialSize = setsPairedEventsList.size();
    if (initialSize != distinctSets.size()) {
      log.info("*** WARNING: non-distinct SetsPairedEvents ***");
      log.info("***  List<SetsPairedEvents>  size: [ {} ]  ***", initialSize);
      log.info("***  future variants will throw exception  ***");
      log.info("*** WARNING: non-distinct SetsPairedEvents ***");
    }

    log.info("flattenSetsPairedEventsToTags returning distinct size: [{}]", distinctSets.size());
    return distinctSets;
  }
}
