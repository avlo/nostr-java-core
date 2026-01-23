package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class FollowSetsEvent extends AddressableEvent implements TagMappedEventIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";
  public static final String MESSAGE = "FollowSetsEvent ctor() is missing a BadgeAwardGenericEvent parameter";
  @Getter
  private final List<BadgeAwardGenericEvent<BadgeDefinitionAwardEvent>> badgeAwardGenericEvents;

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull BadgeAwardGenericEvent<BadgeDefinitionAwardEvent> badgeAwardGenericEvents) {
    this(identity, recipientPublicKey, identifierTag, relay, List.of(badgeAwardGenericEvents), List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent<BadgeDefinitionAwardEvent>> badgeAwardGenericEvents) {
    this(identity, recipientPublicKey, identifierTag, relay, badgeAwardGenericEvents, List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent<BadgeDefinitionAwardEvent>> badgeAwardGenericEvents,
      @NonNull List<BaseTag> baseTags) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, relay, badgeAwardGenericEvents, baseTags, DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull BadgeAwardGenericEvent<BadgeDefinitionAwardEvent> badgeAwardGenericEvents,
      @NonNull String content) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, relay, List.of(badgeAwardGenericEvents), List.of(), content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent<BadgeDefinitionAwardEvent>> badgeAwardGenericEvents,
      @NonNull String content) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, relay, badgeAwardGenericEvents, List.of(), content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent<BadgeDefinitionAwardEvent>> badgeAwardGenericEvents,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.FOLLOW_SETS,
        identifierTag,
        relay,
        Stream.concat(
            Stream.concat(
                TagMappedEventIF.throwIfEmpty(
                        badgeAwardGenericEvents, MESSAGE)
                    .map(FollowSetsEvent::badgeAwardGenericEventAsEventTag),
                Stream.of(
                    new PubKeyTag(recipientPublicKey))),
            baseTags.stream()
                .filter(Predicate.not(EventTag.class::isInstance))
                .filter(Predicate.not(IdentifierTag.class::isInstance))
                .filter(Predicate.not(PubKeyTag.class::isInstance))),
        content);
    this.badgeAwardGenericEvents = badgeAwardGenericEvents;
  }

  public FollowSetsEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<EventTag, BadgeAwardGenericEvent<BadgeDefinitionAwardEvent>> fxn) {
    super(genericEventRecord);
    this.badgeAwardGenericEvents = mapTagsToEvents(this, fxn, EventTag.class);
  }

  public List<EventTag> getContainedAddressableEvents() {
    return badgeAwardGenericEvents.stream()
        .map(FollowSetsEvent::badgeAwardGenericEventAsEventTag)
        .toList();
  }

  public static EventTag badgeAwardGenericEventAsEventTag(@NonNull BadgeAwardGenericEvent<BadgeDefinitionAwardEvent> badgeAwardGenericEvent) {
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
}
