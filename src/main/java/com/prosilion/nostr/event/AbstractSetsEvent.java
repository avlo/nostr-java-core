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
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AbstractSetsEvent<T extends SetsPairedEventTagIF> extends AddressableEvent implements TagMappedEventIF {
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
       .map(AbstractSetsEvent::badgeAwardGenericEventAsEventTag)
       .toList();
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return setsPairedEventsList.getFirst().getAwardRecipientPublicKey();
  }

  public static <T extends SetsPairedEventTagIF> EventTag badgeAwardGenericEventAsEventTag(@NonNull SetsPairedEvents<T> tupleDefnEventAuxAwardEventAux) {
    return new EventTag(
       tupleDefnEventAuxAwardEventAux.getAwardEventId(),
       tupleDefnEventAuxAwardEventAux.getAwardEventRelay().map(Relay::getUrl).orElse(null));
  }

  protected static <T extends SetsPairedEventTagIF> List<BaseTag> buildTags(
     @NonNull List<SetsPairedEvents<T>> setsPairedEventsList,
     @NonNull List<BaseTag> baseTags) {
    return Stream.concat(
       validateIdenticalBadgeAwardGenericEventsPublicKeys(
          setsPairedEventsList).stream(),
       baseTags.stream()
          .filter(Predicate.not(EventTag.class::isInstance))
          .filter(Predicate.not(AddressTag.class::isInstance))).toList();
  }
}
