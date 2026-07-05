package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSetsEvent extends AddressableEvent implements TagMappedEventIF {
  @Getter
  @JsonIgnore
  protected final SetsPairedEvent setsPairedEvent;

  protected AbstractSetsEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull IdentifierTag identifierTag,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull List<BaseTag> tags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       kind,
       identifierTag,
       buildTags(setsPairedEvent, tags),
       content, relay);
    this.setsPairedEvent = setsPairedEvent;
  }

  protected AbstractSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull SetsPairedEvent setsPairedEvent) {
    super(genericEventRecord);
//    this.setsPairedEvents = cullMatchingSetsPairs(setsPairedEvents);
    this.setsPairedEvent = setsPairedEvent;
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
  public final AddressTag getAddressTag() {
    return setsPairedEvent.getAddressTag();
  }

  @JsonIgnore
  public final EventTag getEventTag() {
    return setsPairedEvent.getEventTag();
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return setsPairedEvent.getAwardRecipientPublicKey();
  }

  protected static List<BaseTag> buildTags(
     @NonNull SetsPairedEvent setsPairedEventList,
     @NonNull List<BaseTag> baseTags) {
    return Stream.concat(
       setsPairsToBaseTags(setsPairedEventList),
       baseTags.stream()
          .filter(Predicate.not(EventTag.class::isInstance))
          .filter(Predicate.not(AddressTag.class::isInstance))).toList();
  }

  private static Stream<BaseTag> setsPairsToBaseTags(@NonNull SetsPairedEvent sets) {
    return Stream.of(sets.getAddressTag(), sets.getEventTag());
  }
}
