package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.FollowSetsEventRxR;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.BadgeAwardReputationEventTest.MINUS_ONE_FORMULA;
import static com.prosilion.nostr.BadgeAwardReputationEventTest.PLUS_ONE_FORMULA;
import static com.prosilion.nostr.event.FollowSetsEventRxR.MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FollowSetsEventRxRTest {
  private static final ExternalIdentityTag EXTERNAL_IDENTITY_TAG =
     new ExternalIdentityTag(
        "afterimage",
        "badge_definition_reputation",
        String.valueOf(BadgeDefinitionReputationEvent.class.hashCode()));

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

  BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardUpvoteEventAux;
  BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardDownvoteEvent;
  private final FormulaEvent plusOneFormulaEvent;
  private final FormulaEvent minusOneFormulaEvent;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventPlusOneFormula;
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEventMinusOneFormula;

  public FollowSetsEventRxRTest() throws ParseException {
    BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent =
       new BadgeDefinitionGenericEvent(authorIdentity, upvoteIdentifierTag, relay);

    BadgeDefinitionGenericEventAux badgeDefinitionUpvoteEventAux =
       new BadgeDefinitionGenericEventAux(badgeDefinitionUpvoteEvent, new RelayTag(relay));

    this.badgeAwardUpvoteEventAux = new BadgeAwardGenericEventAux<>(
       authorIdentity,
       upvotedUserPublicKey,
       relay,
       badgeDefinitionUpvoteEventAux);

    BadgeDefinitionGenericEvent badgeDefinitionDownvoteEvent =
       new BadgeDefinitionGenericEvent(authorIdentity, upvoteIdentifierTag, relay);
    BadgeDefinitionGenericEventAux badgeDefinitionDownvoteEventAux = new BadgeDefinitionGenericEventAux(
       badgeDefinitionDownvoteEvent, new RelayTag(relay));

    this.badgeAwardDownvoteEvent = new BadgeAwardGenericEventAux<>(
       authorIdentity,
       upvotedUserPublicKey,
       relay,
       badgeDefinitionDownvoteEventAux);

    this.plusOneFormulaEvent = new FormulaEvent(
       authorIdentity,
       formulaUnitUpvote,
       relay,
       badgeDefinitionUpvoteEvent,
       PLUS_ONE_FORMULA);

    this.minusOneFormulaEvent = new FormulaEvent(
       authorIdentity,
       formulaUnitDownvote,
       relay,
       badgeDefinitionDownvoteEvent,
       MINUS_ONE_FORMULA);

    this.badgeDefinitionReputationEventPlusOneFormula = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       authorIdentity.getPublicKey(),
       FollowSetsEventRxR.defaultIdentifierTag,
       relay,
       EXTERNAL_IDENTITY_TAG,
       plusOneFormulaEvent);

    this.badgeDefinitionReputationEventMinusOneFormula = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       authorIdentity.getPublicKey(),
       FollowSetsEventRxR.defaultIdentifierTag,
       relay,
       EXTERNAL_IDENTITY_TAG,
       minusOneFormulaEvent);
  }

  @Test
  final void testValidFollowSetsEvent() {
    new FollowSetsEventRxR(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       relay,
       List.of(badgeAwardUpvoteEventAux, badgeAwardDownvoteEvent));
  }

  @Test
  final void testFollowSetsEventEquality() {
    List<BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux>> badgeAwardAbstractEvents = List.of(badgeAwardUpvoteEventAux, badgeAwardDownvoteEvent);
    FollowSetsEventRxR expected = new FollowSetsEventRxR(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       relay,
       badgeAwardAbstractEvents);

    FollowSetsEventRxR followSetsEvent = new FollowSetsEventRxR(
       expected.getGenericEventRecord(),
       eventTag ->
          badgeAwardAbstractEvents.stream().filter(badgeAwardAbstractEvent ->
             FollowSetsEventRxR.badgeAwardGenericEventAsEventTag(badgeAwardAbstractEvent).equals(eventTag)).findFirst().orElseThrow(),
       addressTag -> badgeDefinitionReputationEventPlusOneFormula);

    assertEquals(expected.getAddressTag(), followSetsEvent.getAddressTag());
    assertEquals(expected.getBadgeDefinitionReputationEvent(), badgeDefinitionReputationEventPlusOneFormula);
    assertEquals(expected, followSetsEvent);
  }

  @Test
  final void testFollowSetsEventEqualityViaGetContainedAddressableEvents() {
    List<BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux>> badgeAwardGenericEvents = List.of(badgeAwardUpvoteEventAux, badgeAwardDownvoteEvent);
    FollowSetsEventRxR actual = new FollowSetsEventRxR(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       relay,
       badgeAwardGenericEvents);

    assertEquals(
       badgeAwardGenericEvents.stream()
          .map(
             FollowSetsEventRxR::badgeAwardGenericEventAsEventTag).toList(),
       actual.getEventTags());

    assertEquals(
       badgeAwardGenericEvents.stream().map(badgeAwardAbstractEvent ->
          new EventTag(
             badgeAwardAbstractEvent.getId())).toList(),
       actual.getEventTags());
  }

  @Test
  final void tagCountTest() {
    Identity authorIdentity = Identity.generateRandomIdentity();

    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
       authorIdentity,
       upvoteIdentifierTag,
       relay);

    BadgeDefinitionGenericEventAux badgeDefinitionGenericEventAux = new BadgeDefinitionGenericEventAux(
       badgeDefinitionGenericEvent, new RelayTag(relay));

    BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardGenericEventAux = new BadgeAwardGenericEventAux<>(
       authorIdentity,
       upvotedUserPublicKey,
       relay,
       badgeDefinitionGenericEventAux);

    FollowSetsEventRxR followSetsEvent = new
       FollowSetsEventRxR(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       relay,
       badgeAwardGenericEventAux);

    assertEquals(1, followSetsEvent.getEventTags().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());

    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(relay, followSetsEvent.getRelayTag().map(RelayTag::getRelay).orElseThrow());
  }

  @Test
  final void eventTagCountAsListTest() {
    Identity authorIdentity = Identity.generateRandomIdentity();

    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
       authorIdentity,
       upvoteIdentifierTag,
       relay);

    BadgeDefinitionGenericEventAux badgeDefinitionGenericEventAux = new BadgeDefinitionGenericEventAux(
       badgeDefinitionGenericEvent,
       new RelayTag(relay));

    BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardGenericEvent = new BadgeAwardGenericEventAux<>(
       authorIdentity,
       upvotedUserPublicKey,
       relay,
       badgeDefinitionGenericEventAux);

    FollowSetsEventRxR followSetsEvent = new FollowSetsEventRxR(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       relay,
       List.of(badgeAwardGenericEvent),
       FollowSetsEventRxR.class.getSimpleName());

    assertEquals(1, followSetsEvent.getEventTags().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
  }

  @Test
  final void relayTagCountTest() {
    Relay followSetsEventRelay = new Relay("ws://localhost:5555");
    Relay badgeAwardGenericEventRelay = new Relay("ws://localhost:5554");
    Relay badgeDefinitionGenericEventRelay = new Relay("ws://localhost:5553");

    FollowSetsEventRxR followSetsEventWithBaseTags = new FollowSetsEventRxR(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       followSetsEventRelay,
       List.of(
          new BadgeAwardGenericEventAux<>(
             authorIdentity,
             upvotedUserPublicKey,
             badgeAwardGenericEventRelay,
             new BadgeDefinitionGenericEventAux(
                new BadgeDefinitionGenericEvent(
                   authorIdentity,
                   upvoteIdentifierTag,
                   badgeDefinitionGenericEventRelay),
                new RelayTag(badgeDefinitionGenericEventRelay)))),
       List.of(new RelayTag(relay)),
       FollowSetsEventRxR.class.getSimpleName());

    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(1, followSetsEventWithBaseTags.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(followSetsEventRelay, followSetsEventWithBaseTags.getRelayTag().map(RelayTag::getRelay).orElseThrow());
  }

  @Test
  final void testInvalidFollowSetsEventMultipleIdentifierTags() {
    List<BaseTag> baseTags = new ArrayList<>();
    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
    FollowSetsEventRxR followSetsEvent = new FollowSetsEventRxR(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       relay,
       List.of(badgeAwardUpvoteEventAux, badgeAwardDownvoteEvent),
       baseTags);
    assertEquals(1, followSetsEvent.getTypeSpecificTags(IdentifierTag.class).size());
  }

  @Test
  final void testInvalidEmptyBadgeAwardGenericEventsList() {
    assertTrue(
       assertThrows(
          NostrException.class, () -> new FollowSetsEventRxR(
             aImgIdentity,
             badgeDefinitionReputationEventPlusOneFormula,
             relay,
             List.of())
       ).getMessage().contains(MESSAGE));
  }

  @Test
  final void testEventTagCount() {
    assertEquals(2, new FollowSetsEventRxR(
       aImgIdentity,
       badgeDefinitionReputationEventPlusOneFormula,
       relay,
       List.of(badgeAwardUpvoteEventAux, badgeAwardDownvoteEvent),
       List.of(new EventTag(generateRandomHex64String()))).getTypeSpecificTags(EventTag.class).size());
  }

  @Test
  final void testIdenticalPublicKeys() {
    PublicKey nonMatchingPublicKey = Identity.generateRandomIdentity().getPublicKey();
    BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> notMatchingRecipientDownvoteEvent = new BadgeAwardGenericEventAux<>(
       authorIdentity,
       nonMatchingPublicKey,
       relay,
       badgeAwardUpvoteEventAux.getBadgeDefinitionEvent());

    assertTrue(
       assertThrows(NostrException.class, () ->
          new FollowSetsEventRxR(
             aImgIdentity,
             badgeDefinitionReputationEventPlusOneFormula,
             relay,
             List.of(badgeAwardUpvoteEventAux, notMatchingRecipientDownvoteEvent),
             List.of(new EventTag(generateRandomHex64String())))
       ).getMessage().contains(
          String.format(
             FollowSetsEventRxR.PUBKEYS_MUST_MATCH, "2",
             badgeAwardUpvoteEventAux.getAwardRecipientPublicKey())));
  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
