package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.user.Identity;
import java.util.Arrays;
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
       content,
       relay);
    this.setsPairedEvent = setsPairedEvent;
  }

  protected AbstractSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull SetsPairedEvent setsPairedEvent) {
    super(genericEventRecord);
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
  public List<BaseTag> getAddressTagEventTagPairAsBaseTags() {
    return List.of(getAddressTag(), getEventTag());
  }

  @JsonIgnore
  public final AddressTag asAddressableEventAddressTag(ReferenceTag... referenceTag) {
    return new AddressTag(
       getKind(),
       getPublicKey(),
       getIdentifierTag(),
       getRelay().orElse(new Relay(Arrays.stream(referenceTag)
          .findFirst()
          .map(ReferenceTag::getUrl).orElseThrow(() ->
             new NostrException(MISSING_REFERENCE_TAG)))));
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

  protected static AddressTag fillAddressTag(AddressTag addressTag, ReferenceTag referenceTag) {
    return new AddressTag(
       addressTag.getKind(),
       addressTag.getPublicKey(),
       addressTag.getIdentifierTag(),
       new Relay(
          addressTag.findRelay().map(Relay::getUrl).orElse(referenceTag.getUrl())));
  }

  protected static EventTag fillEventTag(EventTag eventTag, ReferenceTag referenceTag) {
    return new EventTag(
       eventTag.getEventId(),
       eventTag.findRelay().map(Relay::getUrl).orElse(referenceTag.getUrl()));
  }
  
  private static Stream<BaseTag> setsPairsToBaseTags(@NonNull SetsPairedEvent sets) {
    return Stream.of(sets.getAddressTag(), sets.getEventTag());
  }
}
