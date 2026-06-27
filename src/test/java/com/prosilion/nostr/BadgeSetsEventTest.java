package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.BadgeSetsEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.SetsPairedEvents;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.BadgeAwardReputationEventTest.MINUS_ONE_FORMULA;
import static com.prosilion.nostr.BadgeAwardReputationEventTest.PLUS_ONE_FORMULA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeSetsEventTest extends BaseEventAuxTest {
  private static final String FORMULA_UNIT_UPVOTE = "FORMULA_UNIT_UPVOTE";
  private static final String FORMULA_UNIT_DOWNVOTE = "FORMULA_UNIT_DOWNVOTE";
  private static final IdentifierTag formulaUnitUpvote = new IdentifierTag(FORMULA_UNIT_UPVOTE);
  private static final IdentifierTag formulaUnitDownvote = new IdentifierTag(FORMULA_UNIT_DOWNVOTE);

  public static final String FOLLOW_SETS_EVENT = "FOLLOW_SETS_EVENT";
  public final IdentifierTag followSetsIdentifierTag = new IdentifierTag(FOLLOW_SETS_EVENT);
  //  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventPlusOneFormula;
//  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventMinusOneFormula;

  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  public BadgeSetsEventTest() throws ParseException {
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(upvoteDefnCreator, formulaUnitUpvote, relayArgRelay, defnEvent_NoNo_Upvote, PLUS_ONE_FORMULA);
    FormulaEvent minusOneFormulaEvent = new FormulaEvent(upvoteDefnCreator, formulaUnitDownvote, relayArgRelay, defnEvent_NoNo_Downvote, MINUS_ONE_FORMULA);

//    this.badgeDefinitionReputationEventPlusOneFormula = new BadgeDefinitionReputationEvent(aImgIdentity, upvoteDefnCreator.getPublicKey(), reputationIdentifierTag, relayArgRelay, EXTERNAL_IDENTITY_TAG, plusOneFormulaEvent);
//    this.badgeDefinitionReputationEventMinusOneFormula = new BadgeDefinitionReputationEvent(aImgIdentity, upvoteDefnCreator.getPublicKey(), reputationIdentifierTag, relayArgRelay, EXTERNAL_IDENTITY_TAG, minusOneFormulaEvent);

    this.badgeDefinitionReputationEvent =
       new BadgeDefinitionReputationEvent(
          submitter,
          upvoteDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          relayArgRelay,
          EXTERNAL_IDENTITY_TAG,
          List.of(plusOneFormulaEvent, minusOneFormulaEvent));
  }

  @Test
  final void testValidBadgeSetsEvent() {
    SetsPairedEvents<BadgeAwardGenericEventAux> tupleUpvoteEvent = new SetsPairedEvents<>(
       defnAuxNo_defnEvent_NoNo_Upvote,
       eventAuxNo_award_NoNo_defn_NoNo_Upvote);

    SetsPairedEvents<BadgeAwardGenericEventAux> tupleDownvoteEvent = new SetsPairedEvents<>(
       defnAuxNo_defnEvent_NoNo_Downvote,
       eventAuxNo_award_NoNo_defn_NoNo_Downvote);

    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       submitter,
       badgeDefinitionReputationEvent,
       relayArgRelay, List.of(tupleUpvoteEvent, tupleDownvoteEvent));

    assertEquals(badgeSetsEvent.getIdentifierTag(), badgeDefinitionReputationEvent.getIdentifierTag());
    assertEquals(relayArgRelay, badgeSetsEvent.getRelay().orElseThrow());

    List<SetsPairedEvents<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes = badgeSetsEvent.getSetsPairedEvents();
    assertTrue(tupleDefnEventAuxAwardEventAuxes.contains(tupleUpvoteEvent));
    assertTrue(tupleDefnEventAuxAwardEventAuxes.contains(tupleDownvoteEvent));
    String upvoteEventId = eventAuxNo_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().getId();
    String downvoteEventId = eventAuxNo_award_NoNo_defn_NoNo_Downvote.getBadgeAwardGenericEvent().getId();

    assertEquals(
       upvoteEventId,
       tupleDefnEventAuxAwardEventAuxes.stream().map(SetsPairedEvents::getAwardEventId).findFirst().orElseThrow());
    assertEquals(
       defnAuxNo_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag(),
       tupleDefnEventAuxAwardEventAuxes.stream().map(SetsPairedEvents::getAddressTag).findFirst().orElseThrow());

    assertEquals(recipient.getPublicKey(), eventAuxNo_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().getAwardRecipientPublicKey());

    assertTrue(badgeSetsEvent.getEventTags().stream().map(EventTag::getIdEvent).toList().contains(upvoteEventId));
    assertTrue(badgeSetsEvent.getEventTags().stream().map(EventTag::getIdEvent).toList().contains(downvoteEventId));

    AddressTag upvoteAsAddressTag = defnAuxNo_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag();
    AddressTag downvoteAsAddressTag = defnAuxNo_defnEvent_NoNo_Downvote.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag();

    assertTrue(badgeSetsEvent.getAddressTags().stream().toList().contains(upvoteAsAddressTag));
    assertTrue(badgeSetsEvent.getAddressTags().stream().toList().contains(downvoteAsAddressTag));

    assertTrue(tupleDefnEventAuxAwardEventAuxes.stream().map(SetsPairedEvents::getDefinitionEventRelay).toList().contains(defnAuxNo_defnEvent_NoNo_Upvote.getRelay()));
    assertTrue(tupleDefnEventAuxAwardEventAuxes.stream().map(SetsPairedEvents::getDefinitionEventRelay).toList().contains(defnAuxNo_defnEvent_NoNo_Downvote.getRelay()));

    assertEquals(
       new AddressTag(
          badgeSetsEvent.getKind(),
          badgeSetsEvent.getPublicKey(),
          badgeSetsEvent.getIdentifierTag(),
          badgeSetsEvent.getRelay().orElse(null)),
       badgeSetsEvent.asAddressableEventAddressTag());

    assertEquals(
       new AddressTag(
          badgeSetsEvent.getKind(),
          badgeSetsEvent.getPublicKey(),
          badgeSetsEvent.getIdentifierTag()),
       badgeSetsEvent.asAddressableEventAddressTag());

    assertEquals(
       new AddressTag(
          badgeSetsEvent.getKind(),
          badgeSetsEvent.getPublicKey(),
          badgeSetsEvent.getIdentifierTag(),
          null),
       badgeSetsEvent.asAddressableEventAddressTag());

    assertEquals(
       new AddressTag(
          badgeSetsEvent.getKind(),
          badgeSetsEvent.getPublicKey(),
          badgeSetsEvent.getIdentifierTag(),
          new Relay("ws://localhost-nomatch:5555")),
       badgeSetsEvent.asAddressableEventAddressTag());

//    assertTrue(badgeSetsEvent.getEventTags().stream().map(EventTag::requireRelay).toList().contains(eventTagRelay));
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
