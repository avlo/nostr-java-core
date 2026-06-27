package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;

public class FollowSetsEvent extends AbstractSetsEvent<BadgeSetsEvent> {
  public static final String DEFAULT_IDENTIFIER = "PROSILION_FOLLOW_SETS_EVENT";
  public static final IdentifierTag defaultIdentifierTag = new IdentifierTag(DEFAULT_IDENTIFIER);
  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";
  public static final String MESSAGE = "FollowSetsEvent ctor() is missing a BadgeAwardGenericEvent parameter";

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull SetsPairedEvents<BadgeSetsEvent> tupleBadgeDefinitionBadgeEvents) {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(tupleBadgeDefinitionBadgeEvents), List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> tupleDefnEventAuxAwardEventAuxes) {
    this(identity, badgeDefinitionReputationEvent, relay, tupleDefnEventAuxAwardEventAuxes, List.of(), DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull List<BaseTag> baseTags) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, tupleDefnEventAuxAwardEventAuxes, baseTags, DEFAULT_CONTENT);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull SetsPairedEvents<BadgeSetsEvent> tupleBadgeDefinitionBadgeEvents,
     @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(tupleBadgeDefinitionBadgeEvents), List.of(), content);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, tupleDefnEventAuxAwardEventAuxes, List.of(), content);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content) throws NostrException {
    super(
       identity,
       Kind.FOLLOW_SETS,
       defaultIdentifierTag,
       tupleDefnEventAuxAwardEventAuxes,
       badgeDefinitionReputationEvent,
       buildTags(tupleDefnEventAuxAwardEventAuxes, badgeDefinitionReputationEvent, baseTags),
       content,
       relay);
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull SetsPairedEvents<BadgeSetsEvent> tupleDefnEventAuxAwardEventAuxes,
     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    this(genericEventRecord, List.of(tupleDefnEventAuxAwardEventAuxes), fxnAddressTag);
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<SetsPairedEvents<BadgeSetsEvent>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    super(genericEventRecord, tupleDefnEventAuxAwardEventAuxes, fxnAddressTag);
  }

  private static List<BaseTag> buildTags(
     List<SetsPairedEvents<BadgeSetsEvent>> tupleDefnEventAuxAwardEventAuxes,
     BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     List<BaseTag> baseTags) {
    return Stream.concat(
       Stream.concat(
          Stream.concat(
             TagMappedEventIF.throwIfEmpty(tupleDefnEventAuxAwardEventAuxes, MESSAGE)
                .map(AbstractSetsEvent::badgeAwardGenericEventAsEventTag),
             Stream.of(
                validateIdenticalBadgeAwardGenericEventsPublicKeys(tupleDefnEventAuxAwardEventAuxes))),
          Stream.of(badgeDefinitionReputationEvent.asAddressableEventAddressTag())),
       filterBaseTags(baseTags)).toList();
  }

  @JsonIgnore
  public final AddressTag getAddressTag() {
    return requireFirstTag(AddressTag.class);
  }
}
