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
import com.prosilion.nostr.tag.ReferencedAbstractEventTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class FollowSetsEvent extends AddressableEvent implements TagMappedEventIF {
  public static final String DEFAULT_IDENTIFIER = "FOLLOW_SETS_EVENT";
  public static final IdentifierTag defaultIdentifierTag = new IdentifierTag(DEFAULT_IDENTIFIER);
  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";
  public static final String MESSAGE = "FollowSetsEvent ctor() is missing a BadgeAwardGenericEvent parameter";
  @JsonIgnore
  private final List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents;
  @JsonIgnore
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull Relay relay,
      @NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvents) {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(badgeAwardGenericEvents), List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents) {
    this(identity, badgeDefinitionReputationEvent, relay, badgeAwardGenericEvents, List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents,
      @NonNull List<BaseTag> baseTags) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, badgeAwardGenericEvents, baseTags, DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull Relay relay,
      @NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvents,
      @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(badgeAwardGenericEvents), List.of(), content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents,
      @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, badgeAwardGenericEvents, List.of(), content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.FOLLOW_SETS,
        defaultIdentifierTag,
        relay,
        Stream.concat(
            Stream.concat(
                Stream.concat(
                    TagMappedEventIF.throwIfEmpty(
                            badgeAwardGenericEvents, MESSAGE)
                        .map(FollowSetsEvent::badgeAwardGenericEventAsEventTag),
                    Stream.of(
                        validateIdenticalBadgeAwardGenericEventsPublicKeys(badgeAwardGenericEvents))),
                Stream.of(badgeDefinitionReputationEvent.asAddressTag())),
            baseTags.stream()
                .filter(Predicate.not(EventTag.class::isInstance))
                .filter(Predicate.not(PubKeyTag.class::isInstance))
                .filter(Predicate.not(AddressTag.class::isInstance))),
        content);
    this.badgeAwardGenericEvents = badgeAwardGenericEvents;
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
  }

  public FollowSetsEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<EventTag, BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> fxnEventTag,
      @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    super(genericEventRecord);
    this.badgeAwardGenericEvents = mapTagsToEvents(this, fxnEventTag, EventTag.class);
    this.badgeDefinitionReputationEvent = mapTagsToEvents(this, fxnAddressTag, AddressTag.class).getFirst();
  }

  @Override
  @JsonIgnore
  public List<? extends ReferencedAbstractEventTag> getContainedAddressableEvents() {
    return Stream.concat(
        ((List<? extends ReferencedAbstractEventTag>) getAddressableEventTags()).stream(),
        Stream.of(((ReferencedAbstractEventTag) getAddressableAddressTag()))).toList();
  }


  public List<EventTag> getAddressableEventTags() {
    return badgeAwardGenericEvents.stream()
        .map(FollowSetsEvent::badgeAwardGenericEventAsEventTag)
        .toList();
  }

  public AddressTag getAddressableAddressTag() {
    return getTypeSpecificTags(AddressTag.class).getFirst();
  }

  @JsonIgnore
  public PublicKey getPubKeyTagPublicKey() {
    return badgeAwardGenericEvents.getFirst().getPubKeyTagPublicKey();
  }

  public static EventTag badgeAwardGenericEventAsEventTag(@NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent) {
    return new EventTag(
        badgeAwardGenericEvent.getId(),
        Optional.ofNullable(
                badgeAwardGenericEvent.getAddressTag().getRelay())
            .orElseThrow(() ->
                new NostrException(
                    String.format(
                        "FollowSetsEvent BadgeAwardGenericEvent / AddressTag [%s] is missing a relay url", badgeAwardGenericEvent)))
            .getUrl());
  }

  public static PubKeyTag validateIdenticalBadgeAwardGenericEventsPublicKeys(@NonNull List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents) {
    List<PublicKey> distinctPublicKeys = badgeAwardGenericEvents.stream().map(BadgeAwardAbstractEvent::getPubKeyTagPublicKey).distinct().toList();
    if (distinctPublicKeys.size() != 1)
      throw new NostrException(
          String.format(
              "FollowSetsEvent BadgeAwardGenericEvents PublicKeys must all match, but instead contained [%s] different keys:\n  [%s]",
              distinctPublicKeys.size(),
              distinctPublicKeys.stream().map(PublicKey::toHexString).collect(Collectors.joining("],\n  ["))));
    return new PubKeyTag(distinctPublicKeys.getFirst());
  }
}
