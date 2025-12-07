package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeDefinitionReputationEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");

  public static final String REPUTATION = "REPUTATION";
  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";

  public final IdentifierTag reputationIdentifierTag = new IdentifierTag(REPUTATION);
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  public final Identity identity = Identity.generateRandomIdentity();
  private final BadgeDefinitionAwardEvent badgeDefnUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag, relay);

  public static final String PLATFORM = BadgeDefinitionReputationEventTest.class.getPackageName();
  public static final String IDENTITY = BadgeDefinitionReputationEventTest.class.getSimpleName();
  public static final String PROOF = String.valueOf(BadgeDefinitionReputationEventTest.class.hashCode());

  public static final String FORMULA_PLUS_ONE = "FORMULA_PLUS_ONE";
  private final IdentifierTag formulaPlusOneIdentifierTag = new IdentifierTag(FORMULA_PLUS_ONE);

  public static final String PLUS_ONE_FORMULA = "+1";
  private final FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, formulaPlusOneIdentifierTag, badgeDefnUpvoteEvent, PLUS_ONE_FORMULA);
  private final ExternalIdentityTag externalIdentityTag = new ExternalIdentityTag(PLATFORM, IDENTITY, PROOF);

  public BadgeDefinitionReputationEventTest() throws ParseException {
  }

  @Test
  void testValidBadgeDefinitionReputationEventWithPopulatedFormulaEvent() {
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
        identity,
        reputationIdentifierTag,
        relay,
        externalIdentityTag,
        plusOneFormulaEvent);

    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
        expected.getGenericEventRecord(),
        addressTag -> plusOneFormulaEvent);

    assertEquals(
        expected.getFormulaEvents(),
        badgeDefinitionReputationEvent.getFormulaEvents());
  }

  @Test
  void testValidBadgeDefinitionReputationEventWithPopulatedFormulaEvents() throws ParseException {
    final String FORMULA_MINUS_ONE = "FORMULA_PLUS_ONE";
    IdentifierTag formulaMinusOneIdentifierTag = new IdentifierTag(FORMULA_MINUS_ONE);
    final String MINUS_ONE_FORMULA = "-1";

    FormulaEvent minusOneFormulaEvent = new FormulaEvent(
        identity,
        formulaMinusOneIdentifierTag,
        new BadgeDefinitionAwardEvent(
            identity,
            new IdentifierTag("UNIT_DOWNVOTE"),
            relay),
        MINUS_ONE_FORMULA);

    List<FormulaEvent> plusOneMinusOneFormulaEvents = List.of(plusOneFormulaEvent, minusOneFormulaEvent);
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
        identity,
        reputationIdentifierTag,
        relay,
        externalIdentityTag,
        plusOneMinusOneFormulaEvents);

    List<FormulaEvent> expectedFormulaEvents = expected.getFormulaEvents();
    List<FormulaEvent> actualFormulaEvents = new BadgeDefinitionReputationEvent(
        expected.getGenericEventRecord(),
        eventTag ->
            Stream.of(plusOneFormulaEvent, minusOneFormulaEvent).filter(formulaEvent ->
                formulaEvent.getBadgeDefinitionAwardEvent().asAddressTag().equals(eventTag)).findFirst().orElseThrow()).getFormulaEvents();

    assertEquals(
        expectedFormulaEvents,
        actualFormulaEvents);
  }

  @Test
  void testValidBadgeDefinitionReputationEvent() {
    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
        identity,
        reputationIdentifierTag,
        relay,
        externalIdentityTag,
        plusOneFormulaEvent);

    assertEquals(externalIdentityTag, badgeDefinitionReputationEvent.getExternalIdentityTag());
  }

  @Test
  void testInequalityEventCopies() throws ParseException {
    BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag, relay);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, formulaPlusOneIdentifierTag, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            relay,
            externalIdentityTag,
            plusOneFormulaEvent),
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            relay,
            externalIdentityTag,
            plusOneFormulaEvent));

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            relay,
            externalIdentityTag,
            plusOneFormulaEvent),
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            relay,
            externalIdentityTag,
            new FormulaEvent(identity, formulaPlusOneIdentifierTag, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA)));
  }

  @Test
  void testInequality() throws ParseException {
    BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag, relay);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, formulaPlusOneIdentifierTag, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            relay,
            externalIdentityTag,
            plusOneFormulaEvent),
        new BadgeDefinitionReputationEvent(
            identity,
            new IdentifierTag("DIFFERENT_REPUTATION"),
            relay,
            externalIdentityTag,
            plusOneFormulaEvent));

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            relay,
            externalIdentityTag,
            plusOneFormulaEvent),
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            relay,
            externalIdentityTag,
            new FormulaEvent(identity, formulaPlusOneIdentifierTag, badgeDefinitionUpvoteEvent, "+2")));

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            relay,
            externalIdentityTag,
            plusOneFormulaEvent),
        plusOneFormulaEvent);

    assertNotEquals(badgeDefinitionUpvoteEvent, plusOneFormulaEvent);
  }

  @Test
  void nonSingularIdentifierTags() throws ParseException {
    BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag, relay);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, formulaPlusOneIdentifierTag, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);
    List<BaseTag> baseTags = new ArrayList<>();
    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
    String message = assertThrows(
        AssertionError.class, () ->
            new BadgeDefinitionReputationEvent(
                identity,
                reputationIdentifierTag,
                relay,
                externalIdentityTag,
                List.of(plusOneFormulaEvent),
                baseTags)).getMessage();
    assertTrue(
        message.contains(
            "List<BaseTag> should contain [1] IdentifierTag but instead has [2]"
        ));
  }

  @Test
  void testDuplicateFormulaEventThrowsException() throws ParseException {
    FormulaEvent duplicatePlusOneFormulaEvent = new FormulaEvent(identity, formulaPlusOneIdentifierTag, badgeDefnUpvoteEvent, PLUS_ONE_FORMULA);
    String message = assertThrows(
        NostrException.class, () ->
            new BadgeDefinitionReputationEvent(
                identity,
                reputationIdentifierTag,
                relay,
                externalIdentityTag,
                List.of(plusOneFormulaEvent, duplicatePlusOneFormulaEvent))).getMessage();

    assertTrue(
        message.contains(
            BadgeDefinitionReputationEvent.MATCHING_IDENTIFIER_TAGS_FOUND
        ));
  }

  @Test
  void testGetTypeSpecificTags() {
    assertEquals(1,
        new BadgeDefinitionAwardEvent(
            identity,
            reputationIdentifierTag, relay).getTypeSpecificTags(IdentifierTag.class).size());
  }
}
