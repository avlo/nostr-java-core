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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class FollowSetsEvent extends AddressableEvent implements TagMappedEventIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";
  @Getter
  private final List<BadgeAwardGenericEvent> badgeAwardGenericEvents;

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull BadgeAwardGenericEvent badgeAwardGenericEvents) {
    this(identity, recipientPublicKey, identifierTag, relay, List.of(badgeAwardGenericEvents), List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent> badgeAwardGenericEvents) {
    this(identity, recipientPublicKey, identifierTag, relay, badgeAwardGenericEvents, List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent> badgeAwardGenericEvents,
      @NonNull List<BaseTag> baseTags) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, relay, badgeAwardGenericEvents, baseTags, DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull BadgeAwardGenericEvent badgeAwardGenericEvents,
      @NonNull String content) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, relay, List.of(badgeAwardGenericEvents), List.of(), content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent> badgeAwardGenericEvents,
      @NonNull String content) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, relay, badgeAwardGenericEvents, List.of(), content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BadgeAwardGenericEvent> badgeAwardGenericEvents,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.FOLLOW_SETS,
        identifierTag,
        relay,
        Stream.concat(
                Stream.concat(
                    badgeAwardGenericEvents.stream()
                        .map(BadgeAwardGenericEvent::getId)
                        .map(EventTag::new),
                    Stream.of(
                        new PubKeyTag(recipientPublicKey))),
                baseTags.stream()
                    .filter(Predicate.not(EventTag.class::isInstance))
                    .filter(Predicate.not(PubKeyTag.class::isInstance)))
            .collect(Collectors.toList()),
        content);
    this.badgeAwardGenericEvents = badgeAwardGenericEvents;
  }

  public FollowSetsEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<EventTag, BadgeAwardGenericEvent> fxn) {
    super(genericEventRecord);
    this.badgeAwardGenericEvents = mapTagsToEvents(this, fxn, EventTag.class);
  }

  public List<EventTag> getContainedAddressableEvents() {
    return badgeAwardGenericEvents.stream()
        .map(BadgeAwardGenericEvent::getId)
        .map(EventTag::new)
        .toList();
  }
}
