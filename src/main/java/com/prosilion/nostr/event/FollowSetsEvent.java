package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.TupleBadgeDefinitionBadgeEvent;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;

@Getter
public class FollowSetsEvent extends AddressableEvent implements TagMappedEventIF {
  public static final String DEFAULT_IDENTIFIER = "PROSILION_FOLLOW_SETS_EVENT";
  public static final IdentifierTag defaultIdentifierTag = new IdentifierTag(DEFAULT_IDENTIFIER);

  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";
  public static final String MESSAGE = "FollowSetsEvent ctor() is missing a BadgeAwardGenericEvent parameter";
  public static final String PUBKEYS_MUST_MATCH =
     "FollowSetsEvent BadgeAwardGenericEvents PublicKeys must all match, but instead contained [%s] different keys:\n  [%s]";
  @JsonIgnore
  private final List<TupleBadgeDefinitionBadgeEvent> tupleBadgeDefinitionBadgeEvents; // eTags
  @JsonIgnore
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent; // aTag

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull TupleBadgeDefinitionBadgeEvent tupleBadgeDefinitionBadgeEvents) {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(tupleBadgeDefinitionBadgeEvents), List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<TupleBadgeDefinitionBadgeEvent> tupleBadgeDefinitionBadgeEvents) {
    this(identity, badgeDefinitionReputationEvent, relay, tupleBadgeDefinitionBadgeEvents, List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<TupleBadgeDefinitionBadgeEvent> tupleBadgeDefinitionBadgeEvents,
     @NonNull List<BaseTag> baseTags) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, tupleBadgeDefinitionBadgeEvents, baseTags, DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull TupleBadgeDefinitionBadgeEvent tupleBadgeDefinitionBadgeEvents,
     @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(tupleBadgeDefinitionBadgeEvents), List.of(), content);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<TupleBadgeDefinitionBadgeEvent> tupleBadgeDefinitionBadgeEvents,
     @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, tupleBadgeDefinitionBadgeEvents, List.of(), content);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<TupleBadgeDefinitionBadgeEvent> tupleBadgeDefinitionBadgeEvents,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content) throws NostrException {
    super(
       identity,
       Kind.FOLLOW_SETS,
       defaultIdentifierTag,
       Stream.concat(
          Stream.concat(
             Stream.concat(
                TagMappedEventIF.throwIfEmpty(
                      tupleBadgeDefinitionBadgeEvents, MESSAGE)
                   .map(FollowSetsEvent::badgeAwardGenericEventAsEventTag),
                Stream.of(
                   validateIdenticalBadgeAwardGenericEventsPublicKeys(tupleBadgeDefinitionBadgeEvents))),
             Stream.of(badgeDefinitionReputationEvent.asAddressableEventAddressTag())),
          baseTags.stream()
             .filter(Predicate.not(EventTag.class::isInstance))
             .filter(Predicate.not(PubKeyTag.class::isInstance))
             .filter(Predicate.not(AddressTag.class::isInstance))).toList(), content, relay
    );
    this.tupleBadgeDefinitionBadgeEvents = tupleBadgeDefinitionBadgeEvents;
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<TupleBadgeDefinitionBadgeEvent> tupleBadgeDefinitionBadgeEvents,
     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    super(genericEventRecord);
    this.tupleBadgeDefinitionBadgeEvents = tupleBadgeDefinitionBadgeEvents;
    this.badgeDefinitionReputationEvent = mapTagsToEvents(this, fxnAddressTag, AddressTag.class).getFirst();
  }

  @JsonIgnore
  public final List<EventTag> getEventTags() {
    return tupleBadgeDefinitionBadgeEvents.stream()
       .map(FollowSetsEvent::badgeAwardGenericEventAsEventTag)
       .toList();
  }

  @JsonIgnore
  public final AddressTag getAddressTag() {
    return requireFirstTag(AddressTag.class);
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPulicKey() {
    return requireFirstTag(PubKeyTag.class).publicKey();
  }

  public static EventTag badgeAwardGenericEventAsEventTag(@NonNull TupleBadgeDefinitionBadgeEvent tupleBadgeDefinitionBadgeEvent) {
    return new EventTag(
       tupleBadgeDefinitionBadgeEvent.getAwardEventId(),
       tupleBadgeDefinitionBadgeEvent.getAwardEventRelay()
          .getUrl());
  }

  public static PubKeyTag validateIdenticalBadgeAwardGenericEventsPublicKeys(@NonNull List<TupleBadgeDefinitionBadgeEvent> tupleBadgeDefinitionBadgeEvents) {
    List<PublicKey> distinctPublicKeys = tupleBadgeDefinitionBadgeEvents.stream()
       .map(ImmutablePair::getLeft)
       .map(BadgeDefinitionGenericEventAux::getBadgeDefinitionGenericEvent)
       .map(BaseEvent::getPublicKey).distinct().toList();
    if (distinctPublicKeys.size() != 1)
      throw new NostrException(
         String.format(
            PUBKEYS_MUST_MATCH,
            distinctPublicKeys.size(),
            distinctPublicKeys.stream().map(PublicKey::toHexString).collect(Collectors.joining("],\n  ["))));
    return new PubKeyTag(distinctPublicKeys.getFirst());
  }
}
