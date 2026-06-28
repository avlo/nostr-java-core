package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import lombok.NonNull;

public class FollowSetsEvent extends AbstractSetsEvent<BadgeSetsEvent> {
  public static final String DEFAULT_IDENTIFIER = "PROSILION_FOLLOW_SETS_EVENT";
  public static final IdentifierTag defaultIdentifierTag = new IdentifierTag(DEFAULT_IDENTIFIER);
  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull SetsPairedEvents<BadgeSetsEvent> setsPairedEvents,
     @NonNull Relay relay) {
    this(identity, List.of(setsPairedEvents), List.of(), DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> setsPairedEventsList,
     @NonNull Relay relay) {
    this(identity, setsPairedEventsList, List.of(), DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> setsPairedEventsList,
     @NonNull List<BaseTag> baseTags,
     @NonNull Relay relay) throws NostrException {
    this(identity, setsPairedEventsList, baseTags, DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull SetsPairedEvents<BadgeSetsEvent> setsPairedEvents,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, List.of(setsPairedEvents), List.of(), content, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> setsPairedEventsList,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, setsPairedEventsList, List.of(), content, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> setsPairedEventsList,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.FOLLOW_SETS,
       defaultIdentifierTag,
       setsPairedEventsList,
       baseTags,
       content,
       relay);
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull SetsPairedEvents<BadgeSetsEvent> setsPairedEvents) {
    this(genericEventRecord, List.of(setsPairedEvents));
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> setsPairedEventsList) {
    super(genericEventRecord, setsPairedEventsList);
  }
}
