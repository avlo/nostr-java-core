package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;

public class BadgeSetsEvent extends AbstractSetsEvent<BadgeAwardGenericEventAux> implements SetsPairedEventTagIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated BadgeSetsEvent";
  public static final String MESSAGE = "BadgeSetsEvent ctor() is missing a BadgeAwardGenericEvent parameter";

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull SetsPairedEvents<BadgeAwardGenericEventAux> setsPairedEvents) {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(setsPairedEvents), List.of(), DEFAULT_CONTENT);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<SetsPairedEvents<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes) {
    this(identity, badgeDefinitionReputationEvent, relay, tupleDefnEventAuxAwardEventAuxes, List.of(), DEFAULT_CONTENT);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<SetsPairedEvents<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull List<BaseTag> baseTags) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, tupleDefnEventAuxAwardEventAuxes, baseTags, DEFAULT_CONTENT);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull SetsPairedEvents<BadgeAwardGenericEventAux> badgeAwardGenericEventAux,
     @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(badgeAwardGenericEventAux), List.of(), content);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<SetsPairedEvents<BadgeAwardGenericEventAux>> badgeAwardGenericEventAuxes,
     @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, badgeAwardGenericEventAuxes, List.of(), content);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<SetsPairedEvents<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content) throws NostrException {
    super(
       identity,
       Kind.BADGE_SETS_EVENT,
       badgeDefinitionReputationEvent.getIdentifierTag(),
       tupleDefnEventAuxAwardEventAuxes,
       badgeDefinitionReputationEvent,
       buildTags(tupleDefnEventAuxAwardEventAuxes, baseTags),
       content,
       relay);
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull SetsPairedEvents<BadgeAwardGenericEventAux> setsPairedEvents,
     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    this(genericEventRecord, List.of(setsPairedEvents), fxnAddressTag);
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<SetsPairedEvents<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    super(genericEventRecord, tupleDefnEventAuxAwardEventAuxes, fxnAddressTag);
  }

  private static List<BaseTag> buildTags(
     List<SetsPairedEvents<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes,
     List<BaseTag> baseTags) {
    return Stream.concat(
       Stream.concat(
          TagMappedEventIF
             .throwIfEmpty(tupleDefnEventAuxAwardEventAuxes, MESSAGE)
             .flatMap(setsPairedEvents ->
                Stream.of(setsPairedEvents.getAddressTag(), setsPairedEvents.getEventTag())),
          Stream.of(
             validateIdenticalBadgeAwardGenericEventsPublicKeys(tupleDefnEventAuxAwardEventAuxes))),
       filterBaseTags(baseTags)).toList();
  }
}
