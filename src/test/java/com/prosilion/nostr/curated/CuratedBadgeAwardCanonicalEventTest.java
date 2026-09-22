package com.prosilion.nostr.curated;

import com.prosilion.nostr.EventTestFixtures;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.BadgeAwardCanonicalEvent;
import com.prosilion.nostr.event.curated.CuratedBadgeAwardCanonicalEvent;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CuratedBadgeAwardCanonicalEventTest extends EventTestFixtures {

  @Test
  final void testValidBadgeSetsEventUsingBadgeAwardCanonicalEvent() {
    BadgeAwardCanonicalEvent award_YesYes_Defn_YesYes_Upvote =
       new BadgeAwardCanonicalEvent(
          submitter,
          recipient.getPublicKey(),
          defnEvent_YesYes_Upvote,
          List.of(baseTagsRelayTag),
          relayArgRelay);

    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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
    CuratedBadgeAwardCanonicalEvent expectedCurationBadgeAwardCanonicalEvent = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       award_YesYes_Defn_YesYes_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    GenericEventRecord genericEventRecord = new GenericEventRecord(
       expectedCurationBadgeAwardCanonicalEvent.getId(),
       expectedCurationBadgeAwardCanonicalEvent.getPublicKey(),
       System.currentTimeMillis(),
       expectedCurationBadgeAwardCanonicalEvent.getKind(),
       expectedCurationBadgeAwardCanonicalEvent.getTags(),
       expectedCurationBadgeAwardCanonicalEvent.getContent(),
       expectedCurationBadgeAwardCanonicalEvent.getSignature());

    CuratedBadgeAwardCanonicalEvent actual = new CuratedBadgeAwardCanonicalEvent(genericEventRecord);

    assertEquals(expectedCurationBadgeAwardCanonicalEvent.getIdentifierTag(), actual.getIdentifierTag());
    assertEquals(aImgIdentity.getPublicKey(), actual.getAddressTag().getPublicKey());
    assertEquals(AbstractSetsEvent.hashedAddressTag(award_YesYes_Defn_YesYes_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag()), actual.getAddressTag().getIdentifierTag());
    assertEquals(expectedCurationBadgeAwardCanonicalEvent.requireFirstTag(AddressTag.class).getIdentifierTag(), actual.getAddressTag().getIdentifierTag());
    assertEquals(expectedCurationBadgeAwardCanonicalEvent.requireFirstTag(AddressTag.class), actual.getAddressTag());
    assertEquals(expectedCurationBadgeAwardCanonicalEvent.getEventTag(), actual.getEventTag());
    assertEquals(expectedCurationBadgeAwardCanonicalEvent.requireFirstTag(RelayTag.class).getRelay(), actual.getEventTag().requireRelay());
    assertEquals(award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey(), actual.getAwardRecipientPublicKey());
    assertEquals(expectedCurationBadgeAwardCanonicalEvent.requireFirstTag(IdentifierTag.class), actual.getIdentifierTag());
    assertTrue(actual.getRelayTag().isPresent());
    assertTrue(actual.getTypeSpecificTags(ReferenceTag.class).isEmpty());
    assertTrue(actual.getAddressTag().findRelay().isPresent());
    assertTrue(actual.getEventTag().findRelay().isPresent());
  }

  @Test
  final void testGenericEventRecordCtorIncorrectKind() {
    assertTrue(
       assertThrows(NostrException.class, () ->
          new CuratedBadgeAwardCanonicalEvent(
             award_NoNo_Defn_NoNo_Upvote.asGenericEventRecord()))
          .getMessage().contains("Incorrect Kind [8] (expected Kind: [30004])"));
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

    BadgeAwardCanonicalEvent award_YesYes_Defn_YesYes_Upvote =
       new BadgeAwardCanonicalEvent(
          submitter,
          recipient.getPublicKey(),
          defnEvent_YesYes_Upvote,
          List.of(baseTagsRelayTag),
          relayArgRelay);

    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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
    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeAwardCanonicalEvent curationSetsDownvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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
    CuratedBadgeAwardCanonicalEvent curatedUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeAwardCanonicalEvent newFromExisting = new CuratedBadgeAwardCanonicalEvent(curatedUpvoteEvent.asGenericEventRecord());

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
    CuratedBadgeAwardCanonicalEvent expected = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeAwardCanonicalEvent actual = new CuratedBadgeAwardCanonicalEvent(expected.asGenericEventRecord());
    assertEquals(expected, actual);
  }

  @Test
  final void testManualConstruction() {
    CuratedBadgeAwardCanonicalEvent expected = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeAwardCanonicalEvent actual = new CuratedBadgeAwardCanonicalEvent(expected.asGenericEventRecord());
    assertEquals(expected, actual);
  }

  @Test
  final void testMissingRelayTag() {
    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
       upvoteDefnCreator,
       upvoteIdentifierTag,
       relay);
    BadgeAwardCanonicalEvent badgeAwardCanonicalEvent = new BadgeAwardCanonicalEvent(
       submitter,
       recipient.getPublicKey(),
       badgeDefinitionGenericEvent);            // <------------------------- no relay

    CuratedBadgeAwardCanonicalEvent curatedBadgeAwardCanonicalEvent = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       badgeAwardCanonicalEvent,
       new ReferenceTag(relayArgUrl),
       new ReferenceTag(relayArgUrl),
       relay);

    assertEquals(recipient.getPublicKey(), curatedBadgeAwardCanonicalEvent.getAwardRecipientPublicKey());
  }

  @Test
  final void testEventCreateNewFromBadgeAwardCanonicalEventAsGenericEventRecordWithoutRelayTag() {
    BadgeDefinitionGenericEvent badgeDefinitionGenericEventWithoutRelayTag = new BadgeDefinitionGenericEvent(
       upvoteDefnCreator,
       upvoteIdentifierTag);  // <------------------------- no relay

    BadgeAwardCanonicalEvent badgeAwardCanonicalEventAndBadgeDefinitionEventBothWithoutRelayTag = new BadgeAwardCanonicalEvent(
       submitter,
       recipient.getPublicKey(),
       badgeDefinitionGenericEventWithoutRelayTag);  // <------------------------- no relay

    BadgeAwardCanonicalEvent setupBadgeAwardCanonicalEventWithoutRelayTag = new BadgeAwardCanonicalEvent(
       badgeAwardCanonicalEventAndBadgeDefinitionEventBothWithoutRelayTag.asGenericEventRecord(),
       addressTag -> badgeDefinitionGenericEventWithoutRelayTag);

    assertEquals(badgeAwardCanonicalEventAndBadgeDefinitionEventBothWithoutRelayTag, setupBadgeAwardCanonicalEventWithoutRelayTag);
  }

  final void testThrowsException() {

  }

//  @Test
//  final void testFollowSetsEventEquality() {
//    List<BadgeAwardCanonicalEvent<BadgeDefinitionGenericEvent>> badgeAwardAbstractEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
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
//             FollowSetsEvent.badgeAwardCanonicalEventAsEventTag(badgeAwardAbstractEvent).equals(eventTag)).findFirst().orElseThrow(),
//       addressTag -> badgeDefinitionReputationEventPlusOneFormula);
//
//    assertEquals(expected.getAddressTag(), followSetsEvent.getAddressTag());
//    assertEquals(expected.getBadgeDefinitionReputationEvent(), badgeDefinitionReputationEventPlusOneFormula);
//    assertEquals(expected, followSetsEvent);
//  }
//
//  @Test
//  final void testFollowSetsEventEqualityViaGetContainedAddressableEvents() {
//    List<BadgeAwardCanonicalEvent<BadgeDefinitionGenericEvent>> badgeAwardCanonicalEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
//    FollowSetsEvent actual = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardCanonicalEvents);
//
//    assertEquals(
//       badgeAwardCanonicalEvents.stream()
//          .map(
//             FollowSetsEvent::badgeAwardCanonicalEventAsEventTag).toList(),
//       actual.getEventTags());
//
//    assertEquals(
//       badgeAwardCanonicalEvents.stream().map(badgeAwardAbstractEvent ->
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
//    BadgeAwardCanonicalEvent badgeAwardCanonicalEvent = new BadgeAwardCanonicalEvent(
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
//       badgeAwardCanonicalEvent);
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
//    BadgeAwardCanonicalEvent badgeAwardCanonicalEvent = new BadgeAwardCanonicalEvent(
//       upvoteDefnCreator,
//       recipient,
//       relay,
//       badgeDefinitionGenericEvent);
//
//    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardCanonicalEvent),
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
//    Relay badgeAwardCanonicalEventRelay = new Relay("ws://localhost:5554");
//    Relay badgeDefinitionGenericEventRelay = new Relay("ws://localhost:5553");
//
//    FollowSetsEvent followSetsEventWithBaseTags = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       followSetsEventRelay,
//       List.of(new BadgeAwardCanonicalEvent(
//          upvoteDefnCreator,
//          recipient,
//          badgeAwardCanonicalEventRelay,
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
//  final void testInvalidEmptyBadgeAwardCanonicalEventsList() {
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
//    BadgeAwardCanonicalEvent notMatchingRecipientDownvoteEvent = new BadgeAwardCanonicalEvent(
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
