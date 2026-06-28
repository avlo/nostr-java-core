package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import lombok.NonNull;

public class BadgeSetsEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated BadgeSetsEvent";
  @JsonIgnore
  protected final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull SetsPairedEvents setsPairedEvents,
     @NonNull Relay relay) {
    this(identity, badgeDefinitionReputationEvent, List.of(setsPairedEvents), List.of(), DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<SetsPairedEvents> setsPairedEventsList,
     @NonNull Relay relay) {
    this(identity, badgeDefinitionReputationEvent, setsPairedEventsList, List.of(), DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<SetsPairedEvents> setsPairedEventsList,
     @NonNull List<BaseTag> baseTags,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, setsPairedEventsList, baseTags, DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull SetsPairedEvents setsPairedEvents,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(setsPairedEvents), List.of(), content, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<SetsPairedEvents> setsPairedEventsList,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, setsPairedEventsList, List.of(), content, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<SetsPairedEvents> setsPairedEventsList,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.BADGE_SETS_EVENT,
       badgeDefinitionReputationEvent.getIdentifierTag(),
       setsPairedEventsList,
       baseTags,
       content,
       relay);
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull SetsPairedEvents setsPairedEvents,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
    this(genericEventRecord, List.of(setsPairedEvents), badgeDefinitionReputationEvent);
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<SetsPairedEvents> setsPairedEvents,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
    super(genericEventRecord, setsPairedEvents);
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
  }
}
