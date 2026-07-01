package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

public class FollowSetsEvent extends AbstractSetsEvent {
  public static final String DEFAULT_IDENTIFIER = "PROSILION_FOLLOW_SETS_EVENT";
  public static final IdentifierTag defaultIdentifierTag = new IdentifierTag(DEFAULT_IDENTIFIER);
  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";

  @Getter
  @JsonIgnore
  private final List<BadgeSetsEvent> badgeSetsEvents;

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
    this.badgeSetsEvents = badgeSetsEvents.stream().distinct().toList();
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
    this.badgeSetsEvents = badgeSetsEvents.stream().distinct().toList();
  }

  public FollowSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull BadgeSetsEvent newBadgeSetsEvent) {
    return createNewFromExisting(identity, List.of(newBadgeSetsEvent));
  }

  public FollowSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull List<BadgeSetsEvent> newBadgeSetsEvents) {
    List<BadgeSetsEvent> appendList = new ArrayList<>(getBadgeSetsEvents());
    appendList.addAll(newBadgeSetsEvents);
    List<BadgeSetsEvent> distinctList = appendList.stream().distinct().toList();
    if (getBadgeSetsEvents().equals(distinctList))
      return this;

    return new FollowSetsEvent(
      identity,
      distinctList,
      getTags(),
      getContent(),
      getRelay().orElseThrow());
  }

  private static List<SetsPairedEvents> mapStream(List<BadgeSetsEvent> badgeSetsEvents) {
    return badgeSetsEvents.stream()
      .map(AbstractSetsEvent::getSetsPairedEventsList)
      .distinct().flatMap(Collection::stream).toList();
  }
}
