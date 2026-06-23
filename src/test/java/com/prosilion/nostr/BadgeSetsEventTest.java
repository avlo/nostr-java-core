package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.BadgeSetsEventV2;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.FollowSetsEventRxR;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.BadgeAwardReputationEventTest.MINUS_ONE_FORMULA;
import static com.prosilion.nostr.BadgeAwardReputationEventTest.PLUS_ONE_FORMULA;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeSetsEventTest {
  private static final ExternalIdentityTag EXTERNAL_IDENTITY_TAG = new ExternalIdentityTag("afterimage", "badge_definition_reputation", String.valueOf(BadgeDefinitionReputationEvent.class.hashCode()));

  public static final Relay relay = new Relay("ws://localhost:5555");
  public static final Identity authorIdentity = Identity.generateRandomIdentity();
  public static final Identity upvotedUserIdentity = Identity.generateRandomIdentity();
  public static final PublicKey upvotedUserPublicKey = upvotedUserIdentity.getPublicKey();
  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  public static final String UNIT_DOWNVOTE = "UNIT_DOWNVOTE";
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
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

  BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent;
  private final FormulaEvent plusOneFormulaEvent;
  private final FormulaEvent minusOneFormulaEvent;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventPlusOneFormula;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventMinusOneFormula;

  public BadgeSetsEventTest() throws ParseException {
    this.badgeDefinitionUpvoteEvent = new BadgeDefinitionGenericEvent(authorIdentity, upvoteIdentifierTag, relay);
    this.badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(authorIdentity, upvotedUserPublicKey, relay, badgeDefinitionUpvoteEvent);

    BadgeDefinitionGenericEvent badgeDefinitionDownvoteEvent = new BadgeDefinitionGenericEvent(authorIdentity, downvoteIdentifierTag, relay);
    this.badgeAwardDownvoteEvent = new BadgeAwardGenericEvent<>(authorIdentity, upvotedUserPublicKey, relay, badgeDefinitionDownvoteEvent);

    this.plusOneFormulaEvent = new FormulaEvent(authorIdentity, formulaUnitUpvote, relay, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);

    this.minusOneFormulaEvent = new FormulaEvent(authorIdentity, formulaUnitDownvote, relay, badgeDefinitionDownvoteEvent, MINUS_ONE_FORMULA);

    this.badgeDefinitionReputationEventPlusOneFormula = new BadgeDefinitionReputationEvent(aImgIdentity, authorIdentity.getPublicKey(), FollowSetsEventRxR.defaultIdentifierTag, relay, EXTERNAL_IDENTITY_TAG, plusOneFormulaEvent);

    this.badgeDefinitionReputationEventMinusOneFormula = new BadgeDefinitionReputationEvent(aImgIdentity, authorIdentity.getPublicKey(), FollowSetsEventRxR.defaultIdentifierTag, relay, EXTERNAL_IDENTITY_TAG, minusOneFormulaEvent);
  }

  @Test
  final void testValidBadgeSetsEvent() {
    Relay eventTagRelayAux = new Relay("ws://localhost-aux-event-tag-relay:5555");
    Relay addressTagRelayAux = new Relay("ws://localhost-aux-address-tag-relay:5555");

    AddressTag upvoteAddressTagAux = new AddressTag(badgeAwardUpvoteEvent.getAddressTag().getKind(), badgeAwardUpvoteEvent.getAddressTag().getPublicKey(), badgeAwardUpvoteEvent.getAddressTag().getIdentifierTag(), addressTagRelayAux);

    AddressTag downvoteAddressTagAux = new AddressTag(badgeAwardDownvoteEvent.getAddressTag().getKind(), badgeAwardDownvoteEvent.getAddressTag().getPublicKey(), badgeAwardDownvoteEvent.getAddressTag().getIdentifierTag(), addressTagRelayAux);

    EventTag upvoteEventTagAux = new EventTag(badgeAwardUpvoteEvent.getId(), eventTagRelayAux.getUrl());

    EventTag downvoteEventTagAux = new EventTag(badgeAwardDownvoteEvent.getId(), eventTagRelayAux.getUrl());

    BadgeDefinitionGenericEventAux definitionUpvoteEventForTruple = fakedBadgeDefinitionGenericEvent(
       new BadgeDefinitionGenericEvent(authorIdentity, upvoteIdentifierTag, relay), upvoteIdentifierTag);

    BadgeDefinitionGenericEventAux definitionDownvoteEventForTruple = fakedBadgeDefinitionGenericEvent(new BadgeDefinitionGenericEvent(authorIdentity, downvoteIdentifierTag, relay), downvoteIdentifierTag);

    BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> awardUpvoteEventAux = badgeAwardGenericEventConstructPair(
       badgeAwardUpvoteEvent, upvoteEventTagAux, definitionUpvoteEventForTruple);

    BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> awardDownvoteEventAux = badgeAwardGenericEventConstructPair(
       badgeAwardDownvoteEvent, downvoteEventTagAux, definitionDownvoteEventForTruple);

    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(aImgIdentity, authorIdentity.getPublicKey(), FollowSetsEventRxR.defaultIdentifierTag, relay, EXTERNAL_IDENTITY_TAG, plusOneFormulaEvent);

    BadgeSetsEventV2 badgeSetsEvent = new BadgeSetsEventV2(aImgIdentity, badgeDefinitionReputationEvent, relay,
       List.of(awardUpvoteEventAux, awardDownvoteEventAux));

    assertEquals(badgeSetsEvent.getIdentifierTag(), badgeDefinitionReputationEventPlusOneFormula.getIdentifierTag());
    Relay first = badgeSetsEvent.getEventTags().stream().map(EventTag::requireRelay).toList().getFirst();

    boolean equals = first.equals(eventTagRelayAux);
//    assertTrue(badgeSetsEvent.getEventTags().stream().map(EventTag::requireRelay).toList().contains(eventTagRelay));
  }

  public static BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardGenericEventConstructPair(
     EventIF event,
     EventTag eventTagAux,
     BadgeDefinitionGenericEventAux badgeDefinitionUpvoteEvent) {

    BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardGenericEvent = new BadgeAwardGenericEventAux<>(
       new GenericEventRecord(
          event.getId(),
          event.getPublicKey(),
          event.getCreatedAt(),
          event.getKind(),
          List.of(new AddressTag(badgeDefinitionUpvoteEvent.getKind(),
                badgeDefinitionUpvoteEvent.getPublicKey(),
                badgeDefinitionUpvoteEvent.getIdentifierTag(),
                badgeDefinitionUpvoteEvent.getRelay().orElseThrow()),
             new RelayTag(eventTagAux.requireRelay()),
             event.requireFirstTag(PubKeyTag.class)),
          event.getContent(),
          event.getSignature()),
       aTag -> badgeDefinitionUpvoteEvent);
    return badgeAwardGenericEvent;
  }

  public static BadgeDefinitionGenericEventAux fakedBadgeDefinitionGenericEvent(
     BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent,
     IdentifierTag identifierTag) {

    GenericEventRecord genericEventRecord =
       new GenericEventRecord(
          badgeDefinitionUpvoteEvent.getId(),
          badgeDefinitionUpvoteEvent.getPublicKey(),
          badgeDefinitionUpvoteEvent.getCreatedAt(),
          badgeDefinitionUpvoteEvent.getKind(),
          List.of(identifierTag),
          badgeDefinitionUpvoteEvent.getContent(),
          badgeDefinitionUpvoteEvent.getSignature());
    BadgeDefinitionGenericEventAux badgeDefinitionGenericEvent = new BadgeDefinitionGenericEventAux(badgeDefinitionUpvoteEvent, new RelayTag(new Relay("ws://localhost-placeholder-for-test")));
    return badgeDefinitionGenericEvent;
  }

//  @Test
//  final void testFollowSetsEventEquality() {
//    List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardAbstractEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
//    FollowSetsEventRxR expected = new FollowSetsEventRxR(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardAbstractEvents);
//
//    FollowSetsEventRxR followSetsEvent = new FollowSetsEventRxR(
//       expected.getGenericEventRecord(),
//       eventTag ->
//          badgeAwardAbstractEvents.stream().filter(badgeAwardAbstractEvent ->
//             FollowSetsEventRxR.badgeAwardGenericEventAsEventTag(badgeAwardAbstractEvent).equals(eventTag)).findFirst().orElseThrow(),
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
//    FollowSetsEventRxR actual = new FollowSetsEventRxR(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardGenericEvents);
//
//    assertEquals(
//       badgeAwardGenericEvents.stream()
//          .map(
//             FollowSetsEventRxR::badgeAwardGenericEventAsEventTag).toList(),
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
//    Identity authorIdentity = Identity.generateRandomIdentity();
//
//    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
//       authorIdentity,
//       upvoteIdentifierTag,
//       relay);
//
//    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
//       authorIdentity,
//       upvotedUserPublicKey,
//       relay,
//       badgeDefinitionGenericEvent);
//
//    FollowSetsEventRxR followSetsEvent = new
//       FollowSetsEventRxR(
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
//    Identity authorIdentity = Identity.generateRandomIdentity();
//
//    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
//       authorIdentity,
//       upvoteIdentifierTag,
//       relay);
//
//    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
//       authorIdentity,
//       upvotedUserPublicKey,
//       relay,
//       badgeDefinitionGenericEvent);
//
//    FollowSetsEventRxR followSetsEvent = new FollowSetsEventRxR(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardGenericEvent),
//       FollowSetsEventRxR.class.getSimpleName());
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
//    FollowSetsEventRxR followSetsEventWithBaseTags = new FollowSetsEventRxR(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       followSetsEventRelay,
//       List.of(new BadgeAwardGenericEvent<>(
//          authorIdentity,
//          upvotedUserPublicKey,
//          badgeAwardGenericEventRelay,
//          new BadgeDefinitionGenericEvent(
//             authorIdentity,
//             upvoteIdentifierTag,
//             badgeDefinitionGenericEventRelay))),
//       List.of(new RelayTag(relay)),
//       FollowSetsEventRxR.class.getSimpleName());
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
//    FollowSetsEventRxR followSetsEvent = new FollowSetsEventRxR(
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
//          NostrException.class, () -> new FollowSetsEventRxR(
//             aImgIdentity,
//             badgeDefinitionReputationEventPlusOneFormula,
//             relay,
//             List.of())
//       ).getMessage().contains(MESSAGE));
//  }
//
//  @Test
//  final void testEventTagCount() {
//    assertEquals(2, new FollowSetsEventRxR(
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
//       authorIdentity,
//       nonMatchingPublicKey,
//       relay,
//       badgeAwardUpvoteEvent.getBadgeDefinitionEvent());
//
//    assertTrue(
//       assertThrows(NostrException.class, () ->
//          new FollowSetsEventRxR(
//             aImgIdentity,
//             badgeDefinitionReputationEventPlusOneFormula,
//             relay,
//             List.of(badgeAwardUpvoteEvent, notMatchingRecipientDownvoteEvent),
//             List.of(new EventTag(generateRandomHex64String())))
//       ).getMessage().contains(
//          String.format(
//             FollowSetsEventRxR.PUBKEYS_MUST_MATCH, "2",
//             badgeAwardUpvoteEvent.getAwardRecipientPublicKey())));
//  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
