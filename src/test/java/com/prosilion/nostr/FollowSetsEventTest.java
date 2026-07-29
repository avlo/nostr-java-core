package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.BadgeSetsEvent;
import com.prosilion.nostr.event.CuratedBadgeAwardGenericEvent;
import com.prosilion.nostr.event.FollowSetsEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.user.Identity;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.BadgeAwardReputationEventTest.MINUS_ONE_FORMULA;
import static com.prosilion.nostr.BadgeAwardReputationEventTest.PLUS_ONE_FORMULA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class FollowSetsEventTest extends EventTestFixtures {
  public static final Relay auxRelay = new Relay("ws://localhost:5555");
  public static final Identity authorIdentity = Identity.generateRandomIdentity();
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_DOWNVOTE);
  private static final String FORMULA_UNIT_UPVOTE = "FORMULA_UNIT_UPVOTE";
  private static final String FORMULA_UNIT_DOWNVOTE = "FORMULA_UNIT_DOWNVOTE";
  private static final IdentifierTag formulaUnitUpvote = new IdentifierTag(FORMULA_UNIT_UPVOTE);
  private static final IdentifierTag formulaUnitDownvote = new IdentifierTag(FORMULA_UNIT_DOWNVOTE);

  public static final String FOLLOW_SETS_EVENT = "FOLLOW_SETS_EVENT";
  public final IdentifierTag followSetsIdentifierTag = new IdentifierTag(FOLLOW_SETS_EVENT);
  public final Identity aImgIdentity = Identity.generateRandomIdentity();

  private final FormulaEvent plusOneFormulaEvent;
  private final FormulaEvent minusOneFormulaEvent;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventPlusOneFormula;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventMinusOneFormula;

  public FollowSetsEventTest() throws ParseException {
    this.plusOneFormulaEvent = new FormulaEvent(
       authorIdentity,
       formulaUnitUpvote,
       defnEvent_NoNo_Upvote,
       PLUS_ONE_FORMULA,
       auxRelay);

    this.minusOneFormulaEvent = new FormulaEvent(
       authorIdentity,
       formulaUnitDownvote,
       defnEvent_NoNo_Downvote,
       MINUS_ONE_FORMULA,
       auxRelay);

    this.badgeDefinitionReputationEventPlusOneFormula = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       authorIdentity.getPublicKey(),
       FollowSetsEvent.defaultIdentifierTag,
       auxRelay,
       EXTERNAL_IDENTITY_TAG,
       plusOneFormulaEvent);

    this.badgeDefinitionReputationEventMinusOneFormula = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       authorIdentity.getPublicKey(),
       FollowSetsEvent.defaultIdentifierTag,
       auxRelay,
       EXTERNAL_IDENTITY_TAG,
       minusOneFormulaEvent);
  }

  @Test
  final void testValidFollowSetsEvent() {
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

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(curationSetsUpvoteEvent, curationSetsDownvoteEvent), relayArgRelay);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
       aImgIdentity,
       badgeSetsEvent,
       auxRelay);

    assertEquals(recipient.getPublicKey(), followSetsEvent.getAwardRecipientPublicKey());
  }

  @Test
  final void testValidFollowSetsEventBadgeSetsEventContainsDuplicate() {
    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       curationSetsUpvoteEvent,
       relayArgRelay);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
       aImgIdentity,
       List.of(badgeSetsEvent, badgeSetsEvent),
       auxRelay);

    assertEquals(1, followSetsEvent.getBadgeSetsEventList().size());
  }

  @Test
  final void testFollowSetsEventEquality() {
    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       curationSetsUpvoteEvent, relayArgRelay);

    FollowSetsEvent expectedFollowSetsEvent = new FollowSetsEvent(
       aImgIdentity,
       badgeSetsEvent,
       auxRelay);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
       expectedFollowSetsEvent.asGenericEventRecord(),
       badgeSetsEvent);

    assertEquals(expectedFollowSetsEvent.getEventTags(), followSetsEvent.getEventTags());
    assertEquals(expectedFollowSetsEvent, followSetsEvent);

    assertEquals(1, followSetsEvent.getEventTags().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());

    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(auxRelay, followSetsEvent.getRelayTag().map(RelayTag::getRelay).orElseThrow());
  }

  @Test
  final void testFollowSetsEventEqualityViaGetContainedAddressableEvents() {
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

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(curationSetsUpvoteEvent, curationSetsDownvoteEvent),
       relayArgRelay
    );

    FollowSetsEvent expectedFollowSetsEvent = new FollowSetsEvent(
       aImgIdentity,
       badgeSetsEvent,
       auxRelay);

    assertEquals(1, expectedFollowSetsEvent.getEventTags().size());
    assertEquals(1, expectedFollowSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(1, expectedFollowSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(1, expectedFollowSetsEvent.getTypeSpecificTags(EventTag.class).size());

    List<BadgeSetsEvent> expectedBadgeSetsEventList = expectedFollowSetsEvent.getBadgeSetsEventList();
    List<CuratedBadgeAwardGenericEvent> expectedCuratedBadgeAwardGenericEventList = expectedBadgeSetsEventList.stream().map(BadgeSetsEvent::getCuratedBadgeAwardGenericEventList).flatMap(Collection::stream).toList();
    List<EventTag> eventTags = expectedCuratedBadgeAwardGenericEventList.stream()
       .map(CuratedBadgeAwardGenericEvent::getEventTags).flatMap(Collection::stream).toList();

    assertTrue(eventTags.contains(eventAuxNo_award_NoNo_defn_NoNo_UpvoteSetsPairedEvent.getEventTag()));
    assertTrue(eventTags.contains(eventAuxNo_award_NoNo_defn_NoNo_Downvote.getEventTag()));

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
       expectedFollowSetsEvent.getGenericEventRecord(),
       badgeSetsEvent);

    assertEquals(expectedFollowSetsEvent, followSetsEvent);
    assertEquals(auxRelay, followSetsEvent.getRelayTag().map(RelayTag::getRelay).orElseThrow());
  }

  @Test
  final void eventTagCountAsListTest() {
    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(
          curationSetsUpvoteEvent,
          curationSetsUpvoteEvent), relayArgRelay);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
       badgeSetsEvent.getGenericEventRecord(),
       badgeSetsEvent);

    assertEquals(1, followSetsEvent.getEventTags().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
  }

  @Test
  final void testContains() {
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

    BadgeSetsEvent badgeSetsUpvoteEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       curationSetsUpvoteEvent, relayArgRelay);

    BadgeSetsEvent badgeSetsDownvoteEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       curationSetsDownvoteEvent, relayArgRelay);

    FollowSetsEvent followSetsUpEvent = new FollowSetsEvent(
       aImgIdentity,
       badgeSetsUpvoteEvent,
       auxRelay);

    FollowSetsEvent followSetsDownEvent = new FollowSetsEvent(
       aImgIdentity,
       badgeSetsDownvoteEvent,
       auxRelay);

    AddressTag defnUpvoteAsAddressTag = defnAuxNo_defnEvent_NoNo_Upvote.getAddressTag();
    AddressTag defnDownvoteAsAddressTag = defnAuxNo_defnEvent_NoNo_Downvote.getAddressTag();

    List<FollowSetsEvent> upvoteFollowSetsEvent = Stream.of(followSetsUpEvent, followSetsDownEvent)
       .filter(followSetsEvent ->
          followSetsEvent
             .getBadgeSetsEventList().stream()
             .map(BadgeSetsEvent::getCuratedBadgeAwardGenericEventList)
             .flatMap(Collection::stream)
             .map(CuratedBadgeAwardGenericEvent::getSetsPairedEvent)
             .map(SetsPairedEvent::getAddressTag).toList()
             .contains(defnUpvoteAsAddressTag)).toList();
    assertEquals(1, upvoteFollowSetsEvent.size());

    List<FollowSetsEvent> downvoteFollowSetsEvent = Stream.of(followSetsUpEvent, followSetsDownEvent)
       .filter(followSetsEvent -> followSetsEvent
          .getBadgeSetsEventList().stream()
          .map(BadgeSetsEvent::getCuratedBadgeAwardGenericEventList)
          .flatMap(Collection::stream)
          .map(CuratedBadgeAwardGenericEvent::getSetsPairedEvent)
          .map(SetsPairedEvent::getAddressTag).toList()
          .contains(defnDownvoteAsAddressTag)).toList();
    assertEquals(1, downvoteFollowSetsEvent.size());

    FollowSetsEvent followSetsBothEvents = new FollowSetsEvent(
       aImgIdentity,
       List.of(badgeSetsUpvoteEvent, badgeSetsDownvoteEvent),
       auxRelay);

    List<FollowSetsEvent> downvoteFollowSetsEvent_2 = Stream.of(followSetsBothEvents)
       .filter(followSetsEvent -> followSetsEvent
          .getBadgeSetsEventList().stream()
          .map(BadgeSetsEvent::getCuratedBadgeAwardGenericEventList)
          .flatMap(Collection::stream)
          .map(CuratedBadgeAwardGenericEvent::getSetsPairedEvent)
          .map(SetsPairedEvent::getAddressTag).toList()
          .contains(defnDownvoteAsAddressTag)).toList();
    assertEquals(1, downvoteFollowSetsEvent_2.size());

    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent2 = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_YesNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsUpvoteEvent2 = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       curationSetsUpvoteEvent2, relayArgRelay);

    FollowSetsEvent followSetsBothEventsWithVariant = new FollowSetsEvent(
       aImgIdentity,
       List.of(badgeSetsUpvoteEvent, badgeSetsUpvoteEvent2),
       auxRelay);

    List<FollowSetsEvent> upvoteFollowSetsEvent_3 = Stream.of(followSetsBothEvents, followSetsBothEventsWithVariant)
       .filter(followSetsEvent -> followSetsEvent
          .getBadgeSetsEventList().stream().peek(event -> System.out.println("BadgeSetsEvents: \n  " + event.createPrettyPrintJson()))
          .map(BadgeSetsEvent::getCuratedBadgeAwardGenericEventList)
          .flatMap(Collection::stream).peek(event -> System.out.println("CurationSetsEvents:\n " + event.createPrettyPrintJson()))
          .map(CuratedBadgeAwardGenericEvent::getSetsPairedEvent).peek(event -> System.out.println("SetsPairedEvents:\n  " + event))
          .map(SetsPairedEvent::getAddressTag).toList()
          .contains(defnUpvoteAsAddressTag)).toList();
    assertEquals(2, upvoteFollowSetsEvent_3.size());

    List<FollowSetsEvent> downvoteFollowSetsEvent_3 = Stream.of(followSetsBothEvents, followSetsBothEventsWithVariant)
       .filter(followSetsEvent -> followSetsEvent
          .getBadgeSetsEventList().stream()
          .map(BadgeSetsEvent::getCuratedBadgeAwardGenericEventList)
          .flatMap(Collection::stream)
          .map(CuratedBadgeAwardGenericEvent::getSetsPairedEvent)
          .map(SetsPairedEvent::getAddressTag).toList()
          .contains(defnDownvoteAsAddressTag)).toList();
    assertEquals(1, downvoteFollowSetsEvent_3.size());

    FollowSetsEvent followSetsContainingMatchingUpvoteEvent = new FollowSetsEvent(
       aImgIdentity,
       List.of(badgeSetsUpvoteEvent, badgeSetsUpvoteEvent2),
       auxRelay);

    String voteEventId = eventAuxNo_award_NoNo_defn_NoNo_UpvoteSetsPairedEvent.getEventTagEventId();
    assertTrue(Stream.of(followSetsUpEvent, followSetsContainingMatchingUpvoteEvent)
       .allMatch(followSetsEvent ->
          followSetsEvent.getBadgeSetsEventList().stream()
             .map(BadgeSetsEvent::getCuratedBadgeAwardGenericEventList)
             .flatMap(Collection::stream)
             .map(CuratedBadgeAwardGenericEvent::getSetsPairedEvent)
             .map(SetsPairedEvent::getEventTagEventId).toList()
             .contains(voteEventId)));

    assertFalse(Stream.of(followSetsDownEvent, followSetsContainingMatchingUpvoteEvent)
       .allMatch(followSetsEvent ->
          followSetsEvent.getBadgeSetsEventList().stream()
             .map(BadgeSetsEvent::getCuratedBadgeAwardGenericEventList)
             .flatMap(Collection::stream)
             .map(CuratedBadgeAwardGenericEvent::getSetsPairedEvent)
             .map(SetsPairedEvent::getEventTagEventId).toList()
             .contains(voteEventId)));
  }

  @Test
  final void testNewFromExisting() {
    CuratedBadgeAwardGenericEvent curationSetsUpvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       curationSetsUpvoteEvent,
       relayArgRelay);

    FollowSetsEvent followSetsUpvoteEvent = new FollowSetsEvent(
       aImgIdentity,
       List.of(badgeSetsEvent, badgeSetsEvent),
       auxRelay);

    FollowSetsEvent newFromExisting = followSetsUpvoteEvent.createNewFromExisting(
       aImgIdentity, badgeSetsEvent);

    assertEquals(followSetsUpvoteEvent.getBadgeSetsEventList(), newFromExisting.getBadgeSetsEventList());
    assertEquals(followSetsUpvoteEvent.getEventTags(), newFromExisting.getEventTags());
    assertEquals(followSetsUpvoteEvent.asAddressableEventAddressTag(), newFromExisting.asAddressableEventAddressTag());
    assertEquals(followSetsUpvoteEvent.getIdentifierTag(), newFromExisting.getIdentifierTag());
    assertEquals(followSetsUpvoteEvent.getAwardRecipientPublicKey(), newFromExisting.getAwardRecipientPublicKey());
    assertEquals(followSetsUpvoteEvent.getBadgeSetsEventList().size(), newFromExisting.getBadgeSetsEventList().size());
    assertEquals(1, followSetsUpvoteEvent.getBadgeSetsEventList().size());
    assertEquals(1, newFromExisting.getBadgeSetsEventList().size());

    CuratedBadgeAwardGenericEvent curationSetsDownvoteEvent = new CuratedBadgeAwardGenericEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Downvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsHasUpvoteDownvoteEvents = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       curationSetsDownvoteEvent,
       relayArgRelay);

    FollowSetsEvent newpUpvoteDownvoteEventsFromExistingUpvote = followSetsUpvoteEvent.createNewFromExisting(
       aImgIdentity, badgeSetsHasUpvoteDownvoteEvents);

    assertTrue(newpUpvoteDownvoteEventsFromExistingUpvote.getBadgeSetsEventList().contains(badgeSetsEvent));
    assertTrue(newpUpvoteDownvoteEventsFromExistingUpvote.getBadgeSetsEventList().contains(badgeSetsHasUpvoteDownvoteEvents));
  }
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
//       List.of(
//          new BadgeAwardGenericEventAux<>(
//             authorIdentity,
//             upvotedUserPublicKey,
//             badgeAwardGenericEventRelay,
//             new BadgeDefinitionGenericEventAux(
//                new BadgeDefinitionGenericEvent(
//                   authorIdentity,
//                   upvoteIdentifierTag,
//                   badgeDefinitionGenericEventRelay),
//                new RelayTag(badgeDefinitionGenericEventRelay)))),
//       List.of(new RelayTag(auxRelay)),
//       FollowSetsEvent.class.getSimpleName());
//
//    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(1, followSetsEventWithBaseTags.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(followSetsEventRelay, followSetsEventWithBaseTags.getRelayTag().map(RelayTag::getRelay).orElseThrow());
//  }
//
//  @Test
//  final void testInvalidFollowSetsEventMultipleIdentifierTags() {
//    List<BaseTag> baseTags = new ArrayList<>();
//    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
//    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       auxRelay,
//       List.of(eventAuxNo_award_NoNo_defn_NoNo_Upvote, eventAuxNo_award_NoNo_defn_NoNo_Downvote),
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
//             auxRelay,
//             List.of())
//       ).getMessage().contains(MESSAGE));
//  }
//
//  @Test
//  final void testEventTagCount() {
//    assertEquals(2, new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       auxRelay,
//       List.of(eventAuxNo_award_NoNo_defn_NoNo_Upvote, eventAuxNo_award_NoNo_defn_NoNo_Downvote),
//       List.of(new EventTag(generateRandomHex64String()))).getTypeSpecificTags(EventTag.class).size());
//  }
//
//  @Test
//  final void testIdenticalPublicKeys() {
//    PublicKey nonMatchingPublicKey = Identity.generateRandomIdentity().getPublicKey();
//    BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> notMatchingRecipientDownvoteEvent = new BadgeAwardGenericEventAux<>(
//       authorIdentity,
//       nonMatchingPublicKey,
//       auxRelay,
//       eventAuxNo_award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey(),
//    award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent(),);
//
//    assertTrue(
//       assertThrows(NostrException.class, () ->
//          new FollowSetsEvent(
//             aImgIdentity,
//             badgeDefinitionReputationEventPlusOneFormula,
//             auxRelay,
//             List.of(eventAuxNo_award_NoNo_defn_NoNo_Upvote, notMatchingRecipientDownvoteEvent),
//             List.of(new EventTag(generateRandomHex64String())))
//       ).getMessage().contains(
//          String.format(
//             FollowSetsEvent.PUBKEYS_MUST_MATCH, "2",
//             eventAuxNo_award_NoNo_defn_NoNo_Upvote.getAwardRecipientPublicKey())));
//  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
