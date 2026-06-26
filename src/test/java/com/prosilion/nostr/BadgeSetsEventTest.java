package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.BadgeSetsEventV2;
import com.prosilion.nostr.event.FollowSetsEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.TupleBadgeDefinitionBadgeEvent;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.BadgeAwardReputationEventTest.MINUS_ONE_FORMULA;
import static com.prosilion.nostr.BadgeAwardReputationEventTest.PLUS_ONE_FORMULA;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeSetsEventTest {
  private static final ExternalIdentityTag EXTERNAL_IDENTITY_TAG = new ExternalIdentityTag("afterimage", "badge_definition_reputation", String.valueOf(BadgeDefinitionReputationEvent.class.hashCode()));
  public static final String REPUTATION = "TEST_REPUTATION";
  public static final IdentifierTag reputationIdentifierTag = new IdentifierTag(REPUTATION);

  private static final String relayArgUrl = "ws://localhost:5555";
  private static final Relay relayArgRelay = new Relay(relayArgUrl);
  private static final RelayTag relayArgRelayTag = new RelayTag(relayArgRelay);

  private static final String baseTagsRelayUrl = "ws://localhost-from-relay-tag:5555";
  private static final Relay baseTagsRelay = new Relay(baseTagsRelayUrl);
  private static final RelayTag baseTagsRelayTag = new RelayTag(baseTagsRelay);

  private static final String auxRelayUrl = "ws://localhost-aux-event-relay:5555";
  private static final Relay auxRelay = new Relay(auxRelayUrl);
  private static final RelayTag auxRelayTag = new RelayTag(auxRelay);

  protected final Identity submitter =
//     Identity.generateRandomIdentity();
     Identity.create("aaa4585483196998204846989544737603523651520600328805626488477202");

  protected final Identity upvoteDefnCreator =
//     Identity.generateRandomIdentity();
     Identity.create("bbb4585483196998204846989544737603523651520600328805626488477202");

  protected final Identity recipient =
//     Identity.generateRandomIdentity();
     Identity.create("ccc4585483196998204846989544737603523651520600328805626488477202");

  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
  public static final String UNIT_DOWNVOTE = "UNIT_DOWNVOTE";
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_DOWNVOTE);

  private static final String FORMULA_UNIT_UPVOTE = "FORMULA_UNIT_UPVOTE";
  private static final String FORMULA_UNIT_DOWNVOTE = "FORMULA_UNIT_DOWNVOTE";
  private static final IdentifierTag formulaUnitUpvote = new IdentifierTag(FORMULA_UNIT_UPVOTE);
  private static final IdentifierTag formulaUnitDownvote = new IdentifierTag(FORMULA_UNIT_DOWNVOTE);

  public static final String FOLLOW_SETS_EVENT = "FOLLOW_SETS_EVENT";
  public final IdentifierTag followSetsIdentifierTag = new IdentifierTag(FOLLOW_SETS_EVENT);
  public final Identity aImgIdentity = Identity.generateRandomIdentity();

  BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent;
  BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardDownvoteEvent;

  BadgeDefinitionGenericEvent badgeDefnUpvoteEvent;
  BadgeDefinitionGenericEvent badgeDefnDownvoteEvent;
  private final FormulaEvent plusOneFormulaEvent;
  private final FormulaEvent minusOneFormulaEvent;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventPlusOneFormula;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventMinusOneFormula;

  private final BadgeAwardGenericEventAux awardUpvoteEventAux;
  private final BadgeAwardGenericEventAux awardDownvoteEventAux;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  public BadgeSetsEventTest() throws ParseException {
    this.badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, relayArgRelay);
    this.badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefnUpvoteEvent,
       relayArgRelay);

    this.badgeDefnDownvoteEvent = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag, relayArgRelay);
    this.badgeAwardDownvoteEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefnDownvoteEvent,
       relayArgRelay);

    this.plusOneFormulaEvent = new FormulaEvent(upvoteDefnCreator, formulaUnitUpvote, relayArgRelay, badgeDefnUpvoteEvent, PLUS_ONE_FORMULA);
    this.minusOneFormulaEvent = new FormulaEvent(upvoteDefnCreator, formulaUnitDownvote, relayArgRelay, badgeDefnDownvoteEvent, MINUS_ONE_FORMULA);

    this.badgeDefinitionReputationEventPlusOneFormula = new BadgeDefinitionReputationEvent(aImgIdentity, upvoteDefnCreator.getPublicKey(), FollowSetsEvent.defaultIdentifierTag, relayArgRelay, EXTERNAL_IDENTITY_TAG, plusOneFormulaEvent);
    this.badgeDefinitionReputationEventMinusOneFormula = new BadgeDefinitionReputationEvent(aImgIdentity, upvoteDefnCreator.getPublicKey(), FollowSetsEvent.defaultIdentifierTag, relayArgRelay, EXTERNAL_IDENTITY_TAG, minusOneFormulaEvent);

    this.awardUpvoteEventAux = new BadgeAwardGenericEventAux(
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent), auxRelay);
    this.awardDownvoteEventAux = new BadgeAwardGenericEventAux(
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnDownvoteEvent), auxRelay);

    this.badgeDefinitionReputationEvent =
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          upvoteDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          relayArgRelay,
          EXTERNAL_IDENTITY_TAG,
          List.of(plusOneFormulaEvent, minusOneFormulaEvent));
  }

  @Test
  final void testValidBadgeSetsEvent() {
    BadgeDefinitionGenericEventAux badgeDefnUpvoteEventAux = new BadgeDefinitionGenericEventAux(badgeDefnUpvoteEvent, auxRelay);
    BadgeDefinitionGenericEventAux badgeDefnDownvoteEventAux = new BadgeDefinitionGenericEventAux(badgeDefnDownvoteEvent, auxRelay);

    TupleBadgeDefinitionBadgeEvent tupleUpvoteEvent = new TupleBadgeDefinitionBadgeEvent(badgeDefnUpvoteEventAux, awardUpvoteEventAux);
    TupleBadgeDefinitionBadgeEvent tupleDownvoteEvent = new TupleBadgeDefinitionBadgeEvent(badgeDefnDownvoteEventAux, awardDownvoteEventAux);

    BadgeSetsEventV2 badgeSetsEvent = new BadgeSetsEventV2(
       aImgIdentity,
       badgeDefinitionReputationEvent,
       relayArgRelay, List.of(tupleUpvoteEvent, tupleDownvoteEvent));

    assertEquals(badgeSetsEvent.getIdentifierTag(), badgeDefinitionReputationEvent.getIdentifierTag());
    assertEquals(relayArgRelay, badgeSetsEvent.getRelay().orElseThrow());

    Relay first = badgeSetsEvent.getEventTags().stream().map(EventTag::requireRelay).toList().getFirst();
    boolean equals = first.equals(auxRelay);

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
