package com.prosilion.nostr;

import com.prosilion.nostr.event.FollowSetsEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.BadgeAwardCanonicalEvent;
import com.prosilion.nostr.event.curated.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.curated.BadgeSetsEvent;
import com.prosilion.nostr.event.curated.CuratedBadgeAwardCanonicalEvent;
import com.prosilion.nostr.event.curated.CuratedFormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.user.Identity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class FollowSetsEventTest extends EventTestFixtures {
  public static final Relay auxRelay = new Relay("ws://localhost:5555");
  public static final Identity authorIdentity = Identity.generateRandomIdentity();
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(EventTestFixtures.FORMULA_UNIT_UPVOTE);
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(EventTestFixtures.FORMULA_UNIT_DOWNVOTE);
  private static final String FORMULA_UNIT_UPVOTE = "FORMULA_UNIT_UPVOTE";
  private static final String FORMULA_UNIT_DOWNVOTE = "FORMULA_UNIT_DOWNVOTE";
  private static final IdentifierTag formulaUnitUpvote = new IdentifierTag(FORMULA_UNIT_UPVOTE);
  private static final IdentifierTag formulaUnitDownvote = new IdentifierTag(FORMULA_UNIT_DOWNVOTE);

  public static final String FOLLOW_SETS_EVENT = "FOLLOW_SETS_EVENT";
  public final IdentifierTag followSetsIdentifierTag = new IdentifierTag(FOLLOW_SETS_EVENT);
  public final Identity aImgIdentity = Identity.generateRandomIdentity();

  private final CuratedFormulaEvent plusOneFormulaEvent;
  private final CuratedFormulaEvent minusOneFormulaEvent;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventPlusOneFormula;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventMinusOneFormula;

  public FollowSetsEventTest() {
    this.plusOneFormulaEvent =
       new CuratedFormulaEvent(
          aImgIdentity,
          new FormulaEvent(
             formulaCreator,
             formulaUnitUpvote,
             defnEvent_NoNo_Upvote,
             PLUS_ONE_FORMULA,
             auxRelay),
          new ReferenceTag(auxRelay.getUrl()),
          auxRelay);

    this.minusOneFormulaEvent = new CuratedFormulaEvent(
       aImgIdentity,
       new FormulaEvent(
          formulaCreator,
          formulaUnitDownvote,
          defnEvent_NoNo_Downvote,
          MINUS_ONE_FORMULA,
          auxRelay),
       new ReferenceTag(auxRelay.getUrl()),
       auxRelay);

    this.badgeDefinitionReputationEventPlusOneFormula = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       authorIdentity.getPublicKey(),
       FollowSetsEvent.defaultIdentifierTag,
       EXTERNAL_IDENTITY_TAG, auxRelay,
       plusOneFormulaEvent);

    this.badgeDefinitionReputationEventMinusOneFormula = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       authorIdentity.getPublicKey(),
       FollowSetsEvent.defaultIdentifierTag,
       EXTERNAL_IDENTITY_TAG, auxRelay,
       minusOneFormulaEvent);
  }

  @Test
  final void testCuratedBadgeAwardCanonicalEventList() {
    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(curationSetsUpvoteEvent), relayArgRelay);

    FollowSetsEvent expectedFollowSetsEvent = new FollowSetsEvent(
       aImgIdentity,
       badgeSetsEvent,
       auxRelay);

    GenericEventRecord genericEventRecord = expectedFollowSetsEvent.asGenericEventRecord();

    FollowSetsEvent actualFollowSetsEvent = new FollowSetsEvent(
       genericEventRecord,
       List.of(badgeSetsEvent));

    assertEquals(expectedFollowSetsEvent, actualFollowSetsEvent);

    assertThrows(NostrException.class, () -> new FollowSetsEvent(
       genericEventRecord,
       List.of()));
  }

  @Test
  final void testValidFollowSetsEvent() {
    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    CuratedBadgeAwardCanonicalEvent curationSetsDownvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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
    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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
    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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

    assertEquals(0, followSetsEvent.getEventTags().size());

    assertEquals(1, followSetsEvent.getAddressTags().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(AddressTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(AddressTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(AddressTag.class).size());

    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(auxRelay, followSetsEvent.getRelayTag().map(RelayTag::getRelay).orElseThrow());
  }

  @Test
  final void testFollowSetsEventEqualityViaGetContainedAddressableEvents() {
    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote,
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    CuratedBadgeAwardCanonicalEvent curationSetsDownvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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

    assertEquals(0, expectedFollowSetsEvent.getEventTags().size());

    assertEquals(1, expectedFollowSetsEvent.getAddressTags().size());
    assertEquals(1, expectedFollowSetsEvent.getTypeSpecificTags(AddressTag.class).size());
    assertEquals(1, expectedFollowSetsEvent.getTags().stream().filter(AddressTag.class::isInstance).toList().size());
    assertEquals(1, expectedFollowSetsEvent.getTypeSpecificTags(AddressTag.class).size());

    List<BadgeSetsEvent> expectedBadgeSetsEventList = expectedFollowSetsEvent.getBadgeSetsEventList();
    List<CuratedBadgeAwardCanonicalEvent> expectedCuratedBadgeAwardCanonicalEventList = expectedBadgeSetsEventList.stream().map(BadgeSetsEvent::getCuratedBadgeAwardCanonicalEventList).flatMap(Collection::stream).toList();
    List<EventTag> eventTags = expectedCuratedBadgeAwardCanonicalEventList.stream()
       .map(CuratedBadgeAwardCanonicalEvent::getEventTags).flatMap(Collection::stream).toList();

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
    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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
    assertTrue(Stream.of(new FollowSetsEvent(
          aImgIdentity,
          new BadgeSetsEvent(
             aImgIdentity,
             badgeDefinitionReputationEventPlusOneFormula,
             new CuratedBadgeAwardCanonicalEvent(
                aImgIdentity,
                award_NoNo_Defn_NoNo_Upvote,
                new ReferenceTag(relayArgRelay.getUrl()),
                new ReferenceTag(relayArgRelay.getUrl()),
                relayArgRelay), relayArgRelay),
          auxRelay), new FollowSetsEvent(
          aImgIdentity,
          List.of(new BadgeSetsEvent(
             aImgIdentity,
             badgeDefinitionReputationEventPlusOneFormula,
             new CuratedBadgeAwardCanonicalEvent(
                aImgIdentity,
                award_NoNo_Defn_NoNo_Upvote,
                new ReferenceTag(relayArgRelay.getUrl()),
                new ReferenceTag(relayArgRelay.getUrl()),
                relayArgRelay), relayArgRelay), new BadgeSetsEvent(
             aImgIdentity,
             badgeDefinitionReputationEventPlusOneFormula,
             new CuratedBadgeAwardCanonicalEvent(
                aImgIdentity,
                award_NoNo_Defn_YesNo_Upvote,
                new ReferenceTag(relayArgRelay.getUrl()),
                new ReferenceTag(relayArgRelay.getUrl()),
                relayArgRelay), relayArgRelay)),
          auxRelay))
       .allMatch(followSetsEvent ->
          followSetsEvent.getBadgeSetsEventList().stream()
             .map(BadgeSetsEvent::getCuratedBadgeAwardCanonicalEventList)
             .flatMap(Collection::stream)
             .map(CuratedBadgeAwardCanonicalEvent::getSetsPairedEvent)
             .map(SetsPairedEvent::getEventTagEventId).toList()
             .contains(eventAuxNo_award_NoNo_defn_NoNo_UpvoteSetsPairedEvent.getEventTagEventId())));

    assertFalse(Stream.of(new FollowSetsEvent(
          aImgIdentity,
          new BadgeSetsEvent(
             aImgIdentity,
             badgeDefinitionReputationEventPlusOneFormula,
             new CuratedBadgeAwardCanonicalEvent(
                aImgIdentity,
                award_NoNo_Defn_NoNo_Downvote,
                new ReferenceTag(relayArgRelay.getUrl()),
                new ReferenceTag(relayArgRelay.getUrl()),
                relayArgRelay), relayArgRelay),
          auxRelay), new FollowSetsEvent(
          aImgIdentity,
          List.of(new BadgeSetsEvent(
             aImgIdentity,
             badgeDefinitionReputationEventPlusOneFormula,
             new CuratedBadgeAwardCanonicalEvent(
                aImgIdentity,
                award_NoNo_Defn_NoNo_Upvote,
                new ReferenceTag(relayArgRelay.getUrl()),
                new ReferenceTag(relayArgRelay.getUrl()),
                relayArgRelay), relayArgRelay), new BadgeSetsEvent(
             aImgIdentity,
             badgeDefinitionReputationEventPlusOneFormula,
             new CuratedBadgeAwardCanonicalEvent(
                aImgIdentity,
                award_NoNo_Defn_YesNo_Upvote,
                new ReferenceTag(relayArgRelay.getUrl()),
                new ReferenceTag(relayArgRelay.getUrl()),
                relayArgRelay), relayArgRelay)),
          auxRelay))
       .allMatch(followSetsEvent ->
          followSetsEvent.getBadgeSetsEventList().stream()
             .map(BadgeSetsEvent::getCuratedBadgeAwardCanonicalEventList)
             .flatMap(Collection::stream)
             .map(CuratedBadgeAwardCanonicalEvent::getSetsPairedEvent)
             .map(SetsPairedEvent::getEventTagEventId).toList()
             .contains(eventAuxNo_award_NoNo_defn_NoNo_UpvoteSetsPairedEvent.getEventTagEventId())));
  }

  @Test
  final void testNewFromExisting() {
    CuratedBadgeAwardCanonicalEvent curationSetsUpvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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

    CuratedBadgeAwardCanonicalEvent curationSetsDownvoteEvent = new CuratedBadgeAwardCanonicalEvent(
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

  @Test
  final void testGetNonMatchingBadgeSetsEventFromBadgeSetsEventLists() {
    CuratedBadgeAwardCanonicalEvent curatedBadgeAwardUpvoteEvent_1 = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       createNewBadgeAwardUpvoteEvent(),
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    CuratedBadgeAwardCanonicalEvent curatedBadgeAwardDownvoteEvent_1 = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       createNewBadgeAwardDownvoteEvent(),
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEvent_1 = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(curatedBadgeAwardUpvoteEvent_1, curatedBadgeAwardDownvoteEvent_1),
       relayArgRelay);

    final IdentifierTag reputationDifferentIdentifierTag = new IdentifierTag("BADGE_DIFFERENT_DEFN_UNIT_REP");

    CuratedBadgeAwardCanonicalEvent curatedBadgeAwardUpvoteEvent_2 = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       createNewBadgeAwardUpvoteEvent(),
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    CuratedBadgeAwardCanonicalEvent curatedBadgeAwardDownvoteEvent_2 = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       createNewBadgeAwardDownvoteEvent(),
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeDefinitionReputationEvent differentBadgeDefinitionReputationEvent =
       new BadgeDefinitionReputationEvent(
          repDefnCreator,
          aImgIdentity.getPublicKey(),
          reputationDifferentIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relayArgRelay,
          List.of(plusOneFormulaEvent, minusOneFormulaEvent));

    BadgeSetsEvent badgeSetsEvent_2 = new BadgeSetsEvent(
       aImgIdentity,
       differentBadgeDefinitionReputationEvent,
       List.of(curatedBadgeAwardUpvoteEvent_2, curatedBadgeAwardDownvoteEvent_2),
       relayArgRelay);

    FollowSetsEvent followSetsEvent_1 = new FollowSetsEvent(
       aImgIdentity,
       List.of(badgeSetsEvent_1, badgeSetsEvent_2),
       auxRelay);

    CuratedBadgeAwardCanonicalEvent curatedBadgeAwardDownvoteEvent_3 = new CuratedBadgeAwardCanonicalEvent(
       aImgIdentity,
       createNewBadgeAwardDownvoteEvent(),
       new ReferenceTag(relayArgRelay.getUrl()),
       new ReferenceTag(relayArgRelay.getUrl()),
       relayArgRelay);

    BadgeSetsEvent badgeSetsEvent_3 = new BadgeSetsEvent(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(curatedBadgeAwardDownvoteEvent_3),
       relayArgRelay);

    FollowSetsEvent followSetsEvent_2 = new FollowSetsEvent(
       aImgIdentity,
       List.of(badgeSetsEvent_3),
       auxRelay);

    FollowSetsEvent actual = createNewFromNonMatching(followSetsEvent_1, followSetsEvent_2);

    assertEquals(1, actual.getBadgeSetsEventList().size());
    assertEquals(
       List.of(curatedBadgeAwardDownvoteEvent_3),
       actual.getBadgeSetsEventList().getFirst().getCuratedBadgeAwardCanonicalEventList());
  }

//   copilot --resume=e40f1fed-c48c-4fd0-81c6-8bcbd3093f1a

  private FollowSetsEvent createNewFromNonMatching(FollowSetsEvent followSetsEventA, FollowSetsEvent followSetsEventB) {
    List<BadgeSetsEvent> setListA = followSetsEventA.getBadgeSetsEventList();
    List<BadgeSetsEvent> setListB = followSetsEventB.getBadgeSetsEventList();
    List<BadgeSetsEvent> nonMatchingSetList = new ArrayList<>();

    for (BadgeSetsEvent setB : setListB) {
      boolean matchingBadgeDefinitionFound = false;

      for (BadgeSetsEvent setA : setListA) {
        if (setA.getBadgeDefinitionReputationEvent()
           .equals(setB.getBadgeDefinitionReputationEvent())) {
          matchingBadgeDefinitionFound = true;
          List<CuratedBadgeAwardCanonicalEvent> filteredSet = filterBNotInA(setA, setB);

          if (!filteredSet.isEmpty()) {
            nonMatchingSetList.add(new BadgeSetsEvent(
               aImgIdentity,
               setB.getBadgeDefinitionReputationEvent(),
               filteredSet,
               setB.getTags(),
               setB.getContent(),
               setB.getRelay().orElseThrow(() ->
                  new NostrException("createNewFromNonMatching BadgeSetsEvent is missing a Relay"))));
          }
        }
      }

      if (!matchingBadgeDefinitionFound) {
        nonMatchingSetList.add(setB);
      }
    }

    if (nonMatchingSetList.isEmpty()) {
      throw new NostrException("createNewFromNonMatching found no non-matching BadgeSetsEvent");
    }

    return new FollowSetsEvent(
       aImgIdentity,
       nonMatchingSetList,
       followSetsEventB.getTags(),
       followSetsEventB.getContent(),
       followSetsEventB.getRelay().orElseThrow(() ->
          new NostrException("createNewFromNonMatching FollowSetsEvent is missing a Relay")));
  }

  private List<CuratedBadgeAwardCanonicalEvent> filterBNotInA(BadgeSetsEvent setA, BadgeSetsEvent setB) {
    return setB.getCuratedBadgeAwardCanonicalEventList().stream()
       .filter(incomingCuratedBadgeAwardVoteEvent ->
          !setA.getCuratedBadgeAwardCanonicalEventList().contains(incomingCuratedBadgeAwardVoteEvent)).toList();
  }

  private BadgeAwardCanonicalEvent createNewBadgeAwardUpvoteEvent() {
    return new BadgeAwardCanonicalEvent(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, relayArgRelay);
  }

  private BadgeAwardCanonicalEvent createNewBadgeAwardDownvoteEvent() {
    return new BadgeAwardCanonicalEvent(submitter, recipient.getPublicKey(), defnEvent_NoNo_Downvote, relayArgRelay);
  }
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
//       List.of(
//          new BadgeAwardCanonicalEventAux<>(
//             authorIdentity,
//             upvotedUserPublicKey,
//             badgeAwardCanonicalEventRelay,
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
//  final void testInvalidEmptyBadgeAwardCanonicalEventsList() {
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
//    BadgeAwardCanonicalEventAux<BadgeDefinitionGenericEventAux> notMatchingRecipientDownvoteEvent = new BadgeAwardCanonicalEventAux<>(
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
