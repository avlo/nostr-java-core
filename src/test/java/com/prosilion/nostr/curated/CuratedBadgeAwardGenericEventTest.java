package com.prosilion.nostr.curated;

import com.prosilion.nostr.EventTestFixtures;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.curated.CuratedBadgeAwardGenericEvent;
import com.prosilion.nostr.event.curated.CuratedBadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CuratedBadgeAwardGenericEventTest extends EventTestFixtures {

  @Test
  final void testValidBadgeSetsEventUsingBadgeAwardGenericEvent() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_YesYes_Upvote =
       new BadgeAwardGenericEvent<>(
          submitter,
          recipient.getPublicKey(),
          defnEvent_YesYes_Upvote,
          List.of(baseTagsRelayTag),
          relayArgRelay);

    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_YesYes_Defn_YesYes_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    SetsPairedEvent setsPairedUpvoteEvent = curationSetsUpvoteEvent.getSetsPairedEvent();
    SetsPairedEvent setsPairedEvent = create(award_YesYes_Defn_YesYes_Upvote, relayArgRelay);
    String upvoteEventId = setsPairedEvent.getEventTagEventId();

    assertEquals(upvoteEventId, setsPairedUpvoteEvent.getEventTagEventId());
    assertEquals(recipient.getPublicKey(), award_YesYes_Defn_YesYes_Upvote.getAwardRecipientPublicKey());
    assertEquals(setsPairedUpvoteEvent.getEventTag().getEventId(), upvoteEventId);

    AddressTag upvoteAsAddressTag = curationSetsUpvoteEvent.getAddressTag();
    assertEquals(curationSetsUpvoteEvent.getAddressTag(), upvoteAsAddressTag);
    assertEquals(setsPairedUpvoteEvent.getDefinitionEventRelay(), setsPairedEvent.getDefinitionEventRelay());
  }

  @Test
  final void testGetIdentifierTagHex64() {
    assertEquals(
       "000000000000000000000000000000000000000000000000000000005187e8c5",
       AbstractSetsEvent.getIdentifierTagHex64(award_YesYes_Defn_YesYes_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag()));
  }

  @Test
  final void testValidBadgeSetsEventUsingGenericEventRecord() {
    CuratedBadgeAwardGenericEvent expectedCurationBadgeAwardGenericEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_YesYes_Defn_YesYes_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    GenericEventRecord genericEventRecord = new GenericEventRecord(
       expectedCurationBadgeAwardGenericEvent.getId(),
       expectedCurationBadgeAwardGenericEvent.getPublicKey(),
       System.currentTimeMillis(),
       expectedCurationBadgeAwardGenericEvent.getKind(),
       expectedCurationBadgeAwardGenericEvent.getTags(),
       expectedCurationBadgeAwardGenericEvent.getContent(),
       expectedCurationBadgeAwardGenericEvent.getSignature());

    CuratedBadgeAwardGenericEvent actual = new CuratedBadgeAwardGenericEvent(genericEventRecord);

    assertEquals(expectedCurationBadgeAwardGenericEvent.getIdentifierTag(), actual.getIdentifierTag());
    assertEquals(aImgIdentity.getPublicKey(), actual.getAddressTag().getPublicKey());
    assertEquals(AbstractSetsEvent.hashedAddressTag(award_YesYes_Defn_YesYes_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag()), actual.getAddressTag().getIdentifierTag());
    assertEquals(expectedCurationBadgeAwardGenericEvent.requireFirstTag(AddressTag.class).getIdentifierTag(), actual.getAddressTag().getIdentifierTag());
    assertEquals(expectedCurationBadgeAwardGenericEvent.requireFirstTag(AddressTag.class), actual.getAddressTag());
    assertEquals(expectedCurationBadgeAwardGenericEvent.getEventTag(), actual.getEventTag());
    assertEquals(expectedCurationBadgeAwardGenericEvent.requireFirstTag(RelayTag.class).getRelay(), actual.getEventTag().requireRelay());
    assertEquals(award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey(), actual.getAwardRecipientPublicKey());
    assertEquals(expectedCurationBadgeAwardGenericEvent.requireFirstTag(IdentifierTag.class), actual.getIdentifierTag());
    assertTrue(actual.getRelayTag().isPresent());
    assertTrue(actual.getTypeSpecificTags(ReferenceTag.class).isEmpty());
    assertTrue(actual.getAddressTag().findRelay().isPresent());
    assertTrue(actual.getEventTag().findRelay().isPresent());
  }

  @Test
  final void testValidBadgeSetsEventUsingCuratedBadgeDefinitionEvent() {
    CuratedBadgeDefinitionGenericEvent curatedBadgeDefinitionGenericEvent = new CuratedBadgeDefinitionGenericEvent(
       aImgIdentity,
       new BadgeDefinitionGenericEvent(
          upvoteDefnCreator,
          upvoteIdentifierTag,
          List.of(baseTagsRelayTag),
          "",
          relayArgRelay),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_YesYes_Upvote =
       new BadgeAwardGenericEvent<>(
          submitter,
          recipient.getPublicKey(),
          defnEvent_YesYes_Upvote,
          List.of(baseTagsRelayTag),
          relayArgRelay);

    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_YesYes_Defn_YesYes_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    SetsPairedEvent setsPairedUpvoteEvent = curationSetsUpvoteEvent.getSetsPairedEvent();
    SetsPairedEvent setsPairedEvent = create(award_YesYes_Defn_YesYes_Upvote, relayArgRelay);
    String upvoteEventId = setsPairedEvent.getEventTagEventId();

    assertEquals(upvoteEventId, setsPairedUpvoteEvent.getEventTagEventId());
    assertEquals(recipient.getPublicKey(), award_YesYes_Defn_YesYes_Upvote.getAwardRecipientPublicKey());
    assertEquals(setsPairedUpvoteEvent.getEventTag().getEventId(), upvoteEventId);

    AddressTag upvoteAsAddressTag = curationSetsUpvoteEvent.getAddressTag();
    assertEquals(curationSetsUpvoteEvent.getAddressTag(), upvoteAsAddressTag);
    assertEquals(setsPairedUpvoteEvent.getDefinitionEventRelay(), setsPairedEvent.getDefinitionEventRelay());
  }

  @Test
  final void testValidBadgeSetsEvent() {
    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeAwardGenericEvent curationSetsDownvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Downvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    SetsPairedEvent setsPairedUpvoteEvent = curationSetsUpvoteEvent.getSetsPairedEvent();
    SetsPairedEvent setsPairedDownvoteEvent = curationSetsDownvoteEvent.getSetsPairedEvent();

    String upvoteEventId = eventAuxNo_award_NoNo_defn_NoNo_UpvoteSetsPairedEvent.getEventTagEventId();
    String downvoteEventId = eventAuxNo_award_NoNo_defn_NoNo_Downvote.getEventTagEventId();

    assertEquals(upvoteEventId, setsPairedUpvoteEvent.getEventTagEventId());
    assertEquals(recipient.getPublicKey(), award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey());

    assertEquals(setsPairedUpvoteEvent.getEventTag().getEventId(), upvoteEventId);
    assertEquals(setsPairedDownvoteEvent.getEventTag().getEventId(), downvoteEventId);

    assertEquals(setsPairedUpvoteEvent.getDefinitionEventRelay(), defnAuxNo_defnEvent_NoNo_Upvote.getDefinitionEventRelay());
    assertEquals(setsPairedDownvoteEvent.getDefinitionEventRelay(), defnAuxNo_defnEvent_NoNo_Downvote.getDefinitionEventRelay());
  }

  @Test
  final void testNewFromExisting() {
    CuratedBadgeAwardGenericEvent curatedUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeAwardGenericEvent newFromExisting = new CuratedBadgeAwardGenericEvent(curatedUpvoteEvent.asGenericEventRecord());

    assertEquals(curatedUpvoteEvent.getAddressTag(), newFromExisting.getAddressTag());
    assertEquals(curatedUpvoteEvent.getAddressTagEventTagPairAsBaseTags(), newFromExisting.getAddressTagEventTagPairAsBaseTags());
    assertEquals(curatedUpvoteEvent.getIdentifierTag(), newFromExisting.getIdentifierTag());
    assertEquals(curatedUpvoteEvent.getEventTag(), newFromExisting.getEventTag());
    assertEquals(curatedUpvoteEvent.asAddressableEventAddressTag(), newFromExisting.asAddressableEventAddressTag());
    assertEquals(curatedUpvoteEvent.getAwardRecipientPublicKey(), newFromExisting.getAwardRecipientPublicKey());
    assertEquals(curatedUpvoteEvent.getIdentifierTag(), newFromExisting.getIdentifierTag());
    assertEquals(curatedUpvoteEvent.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl),
       newFromExisting.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl));
  }

  @Test
  final void testNewFromGenericEventRecord() {
    CuratedBadgeAwardGenericEvent expected = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeAwardGenericEvent actual = new CuratedBadgeAwardGenericEvent(expected.asGenericEventRecord());
    assertEquals(expected, actual);
  }

  @Test
  final void testManualConstruction() {
    CuratedBadgeAwardGenericEvent expected = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeAwardGenericEvent actual = new CuratedBadgeAwardGenericEvent(expected.asGenericEventRecord());
    assertEquals(expected, actual);
  }

  @Test
  final void testMissingRelayTag() {
    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
       upvoteDefnCreator,
       upvoteIdentifierTag,
       relay);
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefinitionGenericEvent);            // <------------------------- no relay

    CuratedBadgeAwardGenericEvent curatedBadgeAwardGenericEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       badgeAwardGenericEvent,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relay);

    assertEquals(recipient.getPublicKey(), curatedBadgeAwardGenericEvent.getAwardRecipientPublicKey());
  }

  @Test
  final void testEventCreateNewFromBadgeAwardGenericEventAsGenericEventRecordWithoutRelayTag() {
    BadgeDefinitionGenericEvent badgeDefinitionGenericEventWithoutRelayTag = new BadgeDefinitionGenericEvent(
       upvoteDefnCreator,
       upvoteIdentifierTag);  // <------------------------- no relay

    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEventAndBadgeDefinitionEventBothWithoutRelayTag = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefinitionGenericEventWithoutRelayTag);  // <------------------------- no relay

    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> setupBadgeAwardGenericEventWithoutRelayTag = new BadgeAwardGenericEvent<>(
       badgeAwardGenericEventAndBadgeDefinitionEventBothWithoutRelayTag.asGenericEventRecord(),
       addressTag -> badgeDefinitionGenericEventWithoutRelayTag);

    assertEquals(badgeAwardGenericEventAndBadgeDefinitionEventBothWithoutRelayTag, setupBadgeAwardGenericEventWithoutRelayTag);
  }

  final void testThrowsException() {

  }

//  @Test
//  final void testFollowSetsEventEquality() {
//    List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardAbstractEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
//    FollowSetsEvent expected = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardAbstractEvents);
//
//    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
//       expected.getGenericEventRecord(),
//       eventTag ->
//          badgeAwardAbstractEvents.stream().filter(badgeAwardAbstractEvent ->
//             FollowSetsEvent.badgeAwardGenericEventAsEventTag(badgeAwardAbstractEvent).equals(eventTag)).findFirst().orElseThrow(),
//       addressTag -> badgeDefinitionReputationEventPlusOneFormula);
//
//    assertEquals(expected.getAddressTag(), followSetsEvent.getAddressTag());
//    assertEquals(expected.getBadgeDefinitionReputationEvent(), badgeDefinitionReputationEventPlusOneFormula);
//    assertEquals(expected, followSetsEvent);
//  }
//
//  @Test
//  final void testFollowSetsEventEqualityViaGetContainedAddressableEvents() {
//    List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
//    FollowSetsEvent actual = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardGenericEvents);
//
//    assertEquals(
//       badgeAwardGenericEvents.stream()
//          .map(
//             FollowSetsEvent::badgeAwardGenericEventAsEventTag).toList(),
//       actual.getEventTags());
//
//    assertEquals(
//       badgeAwardGenericEvents.stream().map(badgeAwardAbstractEvent ->
//          new EventTag(
//             badgeAwardAbstractEvent.getId())).toList(),
//       actual.getEventTags());
//  }
//
//  @Test
//  final void tagCountTest() {
//    Identity upvoteDefnCreator = Identity.generateRandomIdentity();
//
//    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
//       upvoteDefnCreator,
//       upvoteIdentifierTag,
//       relay);
//
//    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
//       upvoteDefnCreator,
//       recipient,
//       relay,
//       badgeDefinitionGenericEvent);
//
//    FollowSetsEvent followSetsEvent = new
//       FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardGenericEvent);
//
//    assertEquals(1, followSetsEvent.getEventTags().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
//    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
//
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(1, followSetsEvent.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(relay, followSetsEvent.getRelay());
//  }
//
//  @Test
//  final void eventTagCountAsListTest() {
//    Identity upvoteDefnCreator = Identity.generateRandomIdentity();
//
//    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
//       upvoteDefnCreator,
//       upvoteIdentifierTag,
//       relay);
//
//    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
//       upvoteDefnCreator,
//       recipient,
//       relay,
//       badgeDefinitionGenericEvent);
//
//    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardGenericEvent),
//       FollowSetsEvent.class.getSimpleName());
//
//    assertEquals(1, followSetsEvent.getEventTags().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
//    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
//  }
//
//  @Test
//  final void relayTagCountTest() {
//    Relay followSetsEventRelay = new Relay("ws://localhost:5555");
//    Relay badgeAwardGenericEventRelay = new Relay("ws://localhost:5554");
//    Relay badgeDefinitionGenericEventRelay = new Relay("ws://localhost:5553");
//
//    FollowSetsEvent followSetsEventWithBaseTags = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       followSetsEventRelay,
//       List.of(new BadgeAwardGenericEvent<>(
//          upvoteDefnCreator,
//          recipient,
//          badgeAwardGenericEventRelay,
//          new BadgeDefinitionGenericEvent(
//             upvoteDefnCreator,
//             upvoteIdentifierTag,
//             badgeDefinitionGenericEventRelay))),
//       List.of(new RelayTag(relay)),
//       FollowSetsEvent.class.getSimpleName());
//
//    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(1, followSetsEventWithBaseTags.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(followSetsEventRelay, followSetsEventWithBaseTags.getRelay());
//  }
//
//  @Test
//  final void testInvalidFollowSetsEventMultipleIdentifierTags() {
//    List<BaseTag> baseTags = new ArrayList<>();
//    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
//    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent),
//       baseTags);
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(IdentifierTag.class).size());
//  }
//
//  @Test
//  final void testInvalidEmptyBadgeAwardGenericEventsList() {
//    assertTrue(
//       assertThrows(
//          NostrException.class, () -> new FollowSetsEvent(
//             aImgIdentity,
//             badgeDefinitionReputationEventPlusOneFormula,
//             relay,
//             List.of())
//       ).getMessage().contains(MESSAGE));
//  }
//
//  @Test
//  final void testEventTagCount() {
//    assertEquals(2, new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent),
//       List.of(new EventTag(generateRandomHex64String()))).getTypeSpecificTags(EventTag.class).size());
//  }
//
//  @Test
//  final void testIdenticalPublicKeys() {
//    PublicKey nonMatchingPublicKey = Identity.generateRandomIdentity().getPublicKey();
//    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> notMatchingRecipientDownvoteEvent = new BadgeAwardGenericEvent<>(
//       upvoteDefnCreator,
//       nonMatchingPublicKey,
//       relay,
//       badgeAwardUpvoteEvent.getBadgeDefinitionEvent());
//
//    assertTrue(
//       assertThrows(NostrException.class, () ->
//          new FollowSetsEvent(
//             aImgIdentity,
//             badgeDefinitionReputationEventPlusOneFormula,
//             relay,
//             List.of(badgeAwardUpvoteEvent, notMatchingRecipientDownvoteEvent),
//             List.of(new EventTag(generateRandomHex64String())))
//       ).getMessage().contains(
//          String.format(
//             FollowSetsEvent.PUBKEYS_MUST_MATCH, "2",
//             badgeAwardUpvoteEvent.getAwardRecipientPublicKey())));
//  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
