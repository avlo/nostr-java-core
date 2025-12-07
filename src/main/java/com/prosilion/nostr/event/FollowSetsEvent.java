package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
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

public class FollowSetsEvent extends UniqueIdentifierTagEvent implements EventTagsMappedEventsIF {
  @Getter
  private final List<BadgeAwardAbstractEvent> badgeAwardAbstractEvents;

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<BadgeAwardAbstractEvent> badgeAwardAbstractEvents,
      @NonNull String content) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, badgeAwardAbstractEvents, List.of(), content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<BadgeAwardAbstractEvent> badgeAwardAbstractEvents,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.FOLLOW_SETS,
        Stream.concat(
                Stream.concat(
                    Stream.concat(
                        badgeAwardAbstractEvents.stream()
                            .map(BadgeAwardAbstractEvent::getId)
                            .map(EventTag::new),
                        Stream.of(
                            new PubKeyTag(recipientPublicKey))),
                    Stream.of(identifierTag)),
                baseTags.stream()
                    .filter(Predicate.not(EventTag.class::isInstance))
                    .filter(Predicate.not(PubKeyTag.class::isInstance)))
            .collect(Collectors.toList()),
        content);
    this.badgeAwardAbstractEvents = badgeAwardAbstractEvents;
  }

  public FollowSetsEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<EventTag, BadgeAwardAbstractEvent> fxn) {
    super(genericEventRecord);
    this.badgeAwardAbstractEvents = mapEventTagsToEvents(this, fxn);
  }

  @Override
  public List<EventTag> getContainedEventsAsEventTags() {
    return badgeAwardAbstractEvents.stream()
        .map(BadgeAwardAbstractEvent::getId)
        .map(EventTag::new)
        .toList();
  }
}
