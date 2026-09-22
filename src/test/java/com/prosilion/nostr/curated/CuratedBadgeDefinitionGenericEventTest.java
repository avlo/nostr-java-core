package com.prosilion.nostr.curated;

import com.prosilion.nostr.EventTestFixtures;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.curated.CuratedBadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CuratedBadgeDefinitionGenericEventTest extends EventTestFixtures {

  @Test
  final void testValidBadgeSetsEventUsingBadgeAwardCanonicalEvent() {
    CuratedBadgeDefinitionGenericEvent curatedBadgeDefinitionGenericEvent = new CuratedBadgeDefinitionGenericEvent(
       aImgIdentity,
       defnEvent_YesYes_Upvote,
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    SetsPairedEvent setsPairedUpvoteEvent = curatedBadgeDefinitionGenericEvent.getSetsPairedEvent();
    assertEquals(defnEvent_YesYes_Upvote.asGenericEventRecord().getId(), setsPairedUpvoteEvent.getEventTagEventId());
    assertEquals(curatedBadgeDefinitionGenericEvent.getAddressTag(), defnEvent_YesYes_Upvote.asAddressableEventAddressTag());
    assertEquals(
       curatedBadgeDefinitionGenericEvent.getIdentifierTag(),
       AbstractSetsEvent.hashedAddressTag(defnEvent_YesYes_Upvote.asAddressableEventAddressTag()));
    assertEquals(
       curatedBadgeDefinitionGenericEvent.getIdentifierTag(),
       AbstractSetsEvent.hashedAddressTag(curatedBadgeDefinitionGenericEvent.getAddressTag()));
    assertEquals(defnEvent_YesYes_Upvote, curatedBadgeDefinitionGenericEvent.getBadgeDefinitionGenericEvent());
  }

  @Test
  final void testNewFromGenericEventRecord() {
    CuratedBadgeDefinitionGenericEvent curatedBadgeDefinitionGenericEvent = new CuratedBadgeDefinitionGenericEvent(
       aImgIdentity,
       defnEvent_YesYes_Upvote,
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    CuratedBadgeDefinitionGenericEvent actual = new CuratedBadgeDefinitionGenericEvent(
       curatedBadgeDefinitionGenericEvent.asGenericEventRecord());

    SetsPairedEvent setsPairedUpvoteEvent = actual.getSetsPairedEvent();
    String upvoteEventId = setsPairedUpvoteEvent.getEventTagEventId();

    assertEquals(upvoteEventId, setsPairedUpvoteEvent.getEventTagEventId());
    assertEquals(recipient.getPublicKey(), award_YesYes_Defn_YesYes_Upvote.getAwardRecipientPublicKey());
    assertEquals(setsPairedUpvoteEvent.getEventTag().getEventId(), upvoteEventId);

    AddressTag upvoteAsAddressTag = award_YesYes_Defn_YesYes_Upvote.getAddressTag();
    assertEquals(actual.getAddressTag(), upvoteAsAddressTag);
    assertEquals(setsPairedUpvoteEvent.getDefinitionEventRelay(), setsPairedUpvoteEvent.getDefinitionEventRelay());
    assertTrue(actual.getRelayTag().isPresent());
    assertTrue(actual.getTypeSpecificTags(ReferenceTag.class).isEmpty());
    assertTrue(actual.getAddressTag().findRelay().isPresent());
    assertTrue(actual.getEventTag().findRelay().isPresent());
  }

  @Test
  final void testGenericEventRecordCtorIncorrectKind() {
    assertTrue(
       assertThrows(NostrException.class, () ->
          new CuratedBadgeDefinitionGenericEvent(
             award_YesYes_Defn_YesYes_Upvote.asGenericEventRecord()))
          .getMessage().contains("Incorrect Kind [8] (expected Kind: [30005])"));
  }

  @Test
  final void testCtorFromBadgeDefinitionGenericEventWoRelayTag() {
    BadgeDefinitionGenericEvent badgeDefinitionUpvoteEventWithoutRelayTag = new BadgeDefinitionGenericEvent(
       aImgIdentity,
       upvoteIdentifierTag);

    CuratedBadgeDefinitionGenericEvent actual = new CuratedBadgeDefinitionGenericEvent(
       aImgIdentity,
       new BadgeDefinitionGenericEvent(badgeDefinitionUpvoteEventWithoutRelayTag.asGenericEventRecord()),
       new ReferenceTag(new Relay("ws://localhost-simualted-from-relay:5555").getUrl()),
       new Relay("ws://localhost-relay-generating-new-curated-event:5555"));

    SetsPairedEvent setsPairedUpvoteEvent = actual.getSetsPairedEvent();
    assertEquals(badgeDefinitionUpvoteEventWithoutRelayTag.getId(), setsPairedUpvoteEvent.getEventTagEventId());

    IdentifierTag upvoteDefinitionIdentifierTag = badgeDefinitionUpvoteEventWithoutRelayTag.getIdentifierTag();
    assertEquals(actual.getAddressTag().getIdentifierTag(), upvoteDefinitionIdentifierTag);
    assertEquals(setsPairedUpvoteEvent.getDefinitionEventRelay(), setsPairedUpvoteEvent.getDefinitionEventRelay());
    assertTrue(actual.getRelayTag().isPresent());
    assertTrue(actual.getTypeSpecificTags(ReferenceTag.class).isEmpty());
    assertTrue(actual.getAddressTag().findRelay().isPresent());
    assertTrue(actual.getEventTag().findRelay().isPresent());
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
//    BadgeAwardCanonicalEvent<BadgeDefinitionGenericEvent> badgeAwardCanonicalEvent = new BadgeAwardCanonicalEvent<>(
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
//    BadgeAwardCanonicalEvent<BadgeDefinitionGenericEvent> badgeAwardCanonicalEvent = new BadgeAwardCanonicalEvent<>(
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
//       List.of(new BadgeAwardCanonicalEvent<>(
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
//    BadgeAwardCanonicalEvent<BadgeDefinitionGenericEvent> notMatchingRecipientDownvoteEvent = new BadgeAwardCanonicalEvent<>(
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
