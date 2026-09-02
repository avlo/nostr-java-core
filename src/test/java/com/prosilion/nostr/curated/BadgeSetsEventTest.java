package com.prosilion.nostr.curated;

import com.prosilion.nostr.EventTestFixtures;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.curated.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.curated.BadgeSetsEvent;
import com.prosilion.nostr.event.curated.CuratedBadgeAwardGenericEvent;
import com.prosilion.nostr.event.curated.CuratedBadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.curated.CuratedFormulaEvent;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeSetsEventTest extends EventTestFixtures {
  public static final String FOLLOW_SETS_EVENT = "FOLLOW_SETS_EVENT";
  public final IdentifierTag followSetsIdentifierTag = new IdentifierTag(FOLLOW_SETS_EVENT);

  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  public BadgeSetsEventTest() {
    CuratedFormulaEvent plusOneFormulaEvent =
       new CuratedFormulaEvent(
          aImgIdentity,
          new FormulaEvent(
             formulaCreator,
             formulaUpvoteIdentifierTag,
             defnEvent_NoNo_Upvote,
             PLUS_ONE_FORMULA,
             relayArgRelay),
          new ReferenceTag(relayArgUrl),
          relayArgRelay);

    CuratedFormulaEvent minusOneFormulaEvent =
       new CuratedFormulaEvent(
          aImgIdentity,
          new FormulaEvent(
             formulaCreator,
             formulaDownvoteIdentifierTag,
             defnEvent_NoNo_Downvote,
             MINUS_ONE_FORMULA,
             relayArgRelay),
          new ReferenceTag(relayArgUrl),
          relayArgRelay);

    this.badgeDefinitionReputationEvent =
       new BadgeDefinitionReputationEvent(
          repDefnCreator,
          aImgIdentity.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relayArgRelay,
          List.of(plusOneFormulaEvent, minusOneFormulaEvent));
  }

  @Test
  final void testValidBadgeSetsEvent() {
    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new CuratedBadgeDefinitionGenericEvent(
          aImgIdentity,
          award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent(),
          new ReferenceTag(relayArgRelay.getUrl()),
          relayArgRelay),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    CuratedBadgeAwardGenericEvent curationSetsDownvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Downvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEvent,
       List.of(curationSetsUpvoteEvent, curationSetsDownvoteEvent),
       relayArgRelay);

    IdentifierTag identifierTag = BadgeSetsEvent.generateIdentifierTag(badgeDefinitionReputationEvent, curationSetsUpvoteEvent.getAwardRecipientPublicKey());

    assertEquals(badgeSetsEvent.getIdentifierTag(), identifierTag);
    assertEquals(badgeSetsEvent.requireFirstTag(AddressTag.class), badgeDefinitionReputationEvent.asAddressableEventAddressTag());
    assertEquals(relayArgRelay, badgeSetsEvent.getRelay().orElseThrow());

    assertTrue(badgeSetsEvent.getCuratedBadgeAwardGenericEventList().stream()
       .map(AbstractSetsEvent::getEventId).anyMatch(curationSetsUpvoteEvent.getId()::equals));
    assertTrue(badgeSetsEvent.getCuratedBadgeAwardGenericEventList().stream()
       .map(AbstractSetsEvent::getEventId).anyMatch(curationSetsDownvoteEvent.getId()::equals));

    assertThrows(NoSuchElementException.class, () ->
       new BadgeSetsEvent(
          aImgIdentity,
          badgeDefinitionReputationEvent,
          List.of(),
          relayArgRelay));
  }

  @Test
  final void testEquality() {
    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    CuratedBadgeAwardGenericEvent curationSetsDownvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Downvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEventWithUpvoteCurationEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEvent,
       curationSetsUpvoteEvent,
       relayArgRelay);

    BadgeSetsEvent newFromExistingHasUpvoteAndDownvote = badgeSetsEventWithUpvoteCurationEvent.createNewFromExisting(aImgIdentity, curationSetsDownvoteEvent);
    assertTrue(newFromExistingHasUpvoteAndDownvote.getCuratedBadgeAwardGenericEventList().contains(curationSetsUpvoteEvent));
    assertTrue(newFromExistingHasUpvoteAndDownvote.getCuratedBadgeAwardGenericEventList().contains(curationSetsDownvoteEvent));

    List<CuratedBadgeAwardGenericEvent> curatedBadgeAwardGenericEventList = List.of(curationSetsUpvoteEvent, curationSetsDownvoteEvent);

    BadgeSetsEvent badgeSetsEventWithUpvoteAndDownvoteCurationSetsEvents = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEvent,
       curatedBadgeAwardGenericEventList,
       relayArgRelay);

    BadgeSetsEvent fromGenericEventRecord = new BadgeSetsEvent(
       badgeSetsEventWithUpvoteAndDownvoteCurationSetsEvents.asGenericEventRecord(),
       badgeDefinitionReputationEvent,
       curatedBadgeAwardGenericEventList);
    assertEquals(badgeSetsEventWithUpvoteAndDownvoteCurationSetsEvents, fromGenericEventRecord);

    BadgeSetsEvent reversedOrder = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEvent,
       List.of(curationSetsDownvoteEvent, curationSetsUpvoteEvent),
       relayArgRelay);
    assertTrue(badgeSetsEventWithUpvoteAndDownvoteCurationSetsEvents.getTags().containsAll(reversedOrder.getTags()));

    BadgeSetsEvent newFromExisting = reversedOrder.createNewFromExisting(
       aImgIdentity,
       curatedBadgeAwardGenericEventList);

    assertTrue(reversedOrder.getTags().containsAll(newFromExisting.getTags()));
  }

  @Test
  final void testGenericEventRecordCtorIncorrectKind() {
    assertTrue(
       assertThrows(NostrException.class, () ->
          new BadgeSetsEvent(
             badgeDefinitionReputationEvent.asGenericEventRecord(),
             badgeDefinitionReputationEvent,
             List.of()))
          .getMessage().contains("Incorrect Kind [30009] (expected Kind: [30008])"));
  }
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
