package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;

public class FollowSetsEvent extends AbstractSetsEvent {
  public static final String DEFAULT_IDENTIFIER = "PROSILION_FOLLOW_SETS_EVENT";
  public static final IdentifierTag defaultIdentifierTag = new IdentifierTag(DEFAULT_IDENTIFIER);
  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeSetsEvent badgeSetsEvent,
     @NonNull Relay relay) {
    this(identity, List.of(badgeSetsEvent), List.of(), DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<BadgeSetsEvent> badgeSetsEvents,
     @NonNull Relay relay) {
    this(identity, badgeSetsEvents, List.of(), DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<BadgeSetsEvent> badgeSetsEvents,
     @NonNull List<BaseTag> baseTags,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeSetsEvents, baseTags, DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeSetsEvent badgeSetsEvent,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, List.of(badgeSetsEvent), List.of(), content, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<BadgeSetsEvent> badgeSetsEvents,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeSetsEvents, List.of(), content, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<BadgeSetsEvent> badgeSetsEvents,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.FOLLOW_SETS,
       defaultIdentifierTag,
       mapStream(badgeSetsEvents),
       baseTags,
       content,
       relay);
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull BadgeSetsEvent badgeSetsEvent) {
    this(genericEventRecord, List.of(badgeSetsEvent));
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<BadgeSetsEvent> badgeSetsEvents) {
    super(genericEventRecord, mapStream(badgeSetsEvents));
  }

  private static List<SetsPairedEvents> mapStream(List<BadgeSetsEvent> badgeSetsEvents) {
    return badgeSetsEvents.stream().map(AbstractSetsEvent::getSetsPairedEventsList).flatMap(Collection::stream).toList();
  }
}
