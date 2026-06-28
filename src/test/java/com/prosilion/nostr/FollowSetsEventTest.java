package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.BadgeSetsEvent;
import com.prosilion.nostr.event.FollowSetsEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.BadgeAwardReputationEventTest.MINUS_ONE_FORMULA;
import static com.prosilion.nostr.BadgeAwardReputationEventTest.PLUS_ONE_FORMULA;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FollowSetsEventTest extends BaseEventAuxTest {
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
       auxRelay,
       defnEvent_NoNo_Upvote,
       PLUS_ONE_FORMULA);

    this.minusOneFormulaEvent = new FormulaEvent(
       authorIdentity,
       formulaUnitDownvote,
       auxRelay,
       defnEvent_NoNo_Downvote,
       MINUS_ONE_FORMULA);

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
    SetsPairedEvents tupleUpvoteEvent = new SetsPairedEvents(
       defnAuxNo_defnEvent_NoNo_Upvote,
       eventAuxNo_award_NoNo_defn_NoNo_Upvote);

    SetsPairedEvents tupleDownvoteEvent = new SetsPairedEvents(
       defnAuxNo_defnEvent_NoNo_Downvote,
       eventAuxNo_award_NoNo_defn_NoNo_Downvote);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       submitter,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(tupleUpvoteEvent, tupleDownvoteEvent), relayArgRelay);

    new FollowSetsEvent(
       aImgIdentity,
       badgeSetsEvent,
       auxRelay);
  }

  @Test
  final void testFollowSetsEventEquality() {
    SetsPairedEvents badgeSetsEventPairedEvents = new SetsPairedEvents(
       defnAuxNo_defnEvent_NoNo_Upvote,
       eventAuxNo_award_NoNo_defn_NoNo_Upvote);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       submitter,
       badgeDefinitionReputationEventPlusOneFormula,
       badgeSetsEventPairedEvents, relayArgRelay);

    FollowSetsEvent expectedFollowSetsEvent = new FollowSetsEvent(
       aImgIdentity,
       badgeSetsEvent,
       auxRelay);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
       expectedFollowSetsEvent.asGenericEventRecord(),
       badgeSetsEvent);

    assertEquals(expectedFollowSetsEvent.getAddressTags(), followSetsEvent.getAddressTags());
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
    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       submitter,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(
          new SetsPairedEvents(
             defnAuxNo_defnEvent_NoNo_Upvote,
             eventAuxNo_award_NoNo_defn_NoNo_Upvote),
          new SetsPairedEvents(
             defnAuxNo_defnEvent_NoNo_Downvote,
             eventAuxNo_award_NoNo_defn_NoNo_Downvote)), relayArgRelay
    );

    FollowSetsEvent expectedFollowSetsEvent = new FollowSetsEvent(
       aImgIdentity,
       badgeSetsEvent,
       auxRelay);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
       expectedFollowSetsEvent.getGenericEventRecord(),
       badgeSetsEvent);

    assertEquals(expectedFollowSetsEvent.getAddressTags(), followSetsEvent.getAddressTags());
    assertEquals(expectedFollowSetsEvent, followSetsEvent);

    assertEquals(2, followSetsEvent.getEventTags().size());
    assertEquals(2, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(2, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(2, followSetsEvent.getTypeSpecificTags(EventTag.class).size());

    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(auxRelay, followSetsEvent.getRelayTag().map(RelayTag::getRelay).orElseThrow());
  }

  @Test
  final void eventTagCountAsListTest() {
    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       submitter,
       badgeDefinitionReputationEventPlusOneFormula,
       List.of(
          new SetsPairedEvents(
             defnAuxNo_defnEvent_NoNo_Upvote,
             eventAuxNo_award_NoNo_defn_NoNo_Upvote),
          new SetsPairedEvents(
             defnAuxNo_defnEvent_NoNo_Upvote,
             eventAuxNo_award_NoNo_defn_NoNo_Upvote)), relayArgRelay);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
       badgeSetsEvent.getGenericEventRecord(),
       badgeSetsEvent);

    assertEquals(1, followSetsEvent.getEventTags().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
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
//       eventAuxNo_award_NoNo_defn_NoNo_Upvote.getBadgeDefinitionEvent());
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
