package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.AddressableEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.BaseEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
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
  public static final String UNIT_DOWNVOTE = "UNIT_DOWNVOTE";

  public final IdentifierTag reputationIdentifierTag = new IdentifierTag(REPUTATION);
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_DOWNVOTE);

  public final Identity aImgidentity = Identity.generateRandomIdentity();
  private final BadgeDefinitionGenericEvent badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, relay);
  private final BadgeDefinitionGenericEvent badgeDefnDownvoteEvent = new BadgeDefinitionGenericEvent(aImgidentity, downvoteIdentifierTag, relay);

  private final PublicKey definitionCreatorPublicKey = // Identity.generateRandomIdentity();
     Identity.create("bbb4585483196998204846989544737603523651520600328805626488477202").getPublicKey();

  public static final String PLATFORM = BadgeDefinitionReputationEventTest.class.getPackageName();
  public static final String IDENTITY = BadgeDefinitionReputationEventTest.class.getSimpleName();
  public static final String PROOF = String.valueOf(BadgeDefinitionReputationEventTest.class.hashCode());

  public static final String FORMULA_PLUS_ONE = "FORMULA_PLUS_ONE";
  public static final String FORMULA_MINUS_ONE = "FORMULA_MINUS_ONE";
  private final IdentifierTag formulaPlusOneIdentifierTag = new IdentifierTag(FORMULA_PLUS_ONE);
  private final IdentifierTag formulaMinusOneIdentifierTag = new IdentifierTag(FORMULA_MINUS_ONE);

  public static final String PLUS_ONE_FORMULA = "+1";
  public static final String MINUS_ONE_FORMULA = "-1";
  private final FormulaEvent plusOneFormulaEvent = new FormulaEvent(aImgidentity, formulaPlusOneIdentifierTag, relay, badgeDefnUpvoteEvent, PLUS_ONE_FORMULA);
  private final FormulaEvent minusOneFormulaEvent = new FormulaEvent(aImgidentity, formulaMinusOneIdentifierTag, relay, badgeDefnDownvoteEvent, MINUS_ONE_FORMULA);
  private final ExternalIdentityTag externalIdentityTag = new ExternalIdentityTag(PLATFORM, IDENTITY, PROOF);

  public BadgeDefinitionReputationEventTest() throws ParseException {
  }

  @Test
  void testValidBadgeDefinitionReputationEventWithPlusOneFormulaEvent() {
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgidentity,
       definitionCreatorPublicKey,
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
  void testValidBadgeDefinitionReputationEventWithPlusOneMinusOneFormulaEvent() {
    List<FormulaEvent> formulaEvents = List.of(plusOneFormulaEvent, minusOneFormulaEvent);
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgidentity,
       definitionCreatorPublicKey,
       reputationIdentifierTag,
       relay,
       externalIdentityTag,
       formulaEvents);

    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       expected.getGenericEventRecord(),
       addressTag ->
          formulaEvents.stream().filter(formulaEvent ->
                addressTag.equals(formulaEvent.asAddressableEventAddressTag()))
             .findFirst().orElseThrow());

    assertEquals(
       expected.getFormulaEvents(),
       badgeDefinitionReputationEvent.getFormulaEvents());
  }

  @Test
  void testValidBadgeDefinitionReputationEventWithPlusOneMinusOneFormulaEventUsingVarArgs() {
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgidentity,
       definitionCreatorPublicKey,
       reputationIdentifierTag,
       relay,
       externalIdentityTag,
       plusOneFormulaEvent, minusOneFormulaEvent);

    List<FormulaEvent> formulaEvents = List.of(plusOneFormulaEvent, minusOneFormulaEvent);
    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       expected.getGenericEventRecord(),
       addressTag ->
          formulaEvents.stream().filter(formulaEvent ->
                addressTag.equals(formulaEvent.asAddressableEventAddressTag()))
             .findFirst().orElseThrow());

    assertEquals(2, expected.getFormulaEvents().size());
    assertEquals(2, badgeDefinitionReputationEvent.getFormulaEvents().size());

    assertTrue(expected.getFormulaEvents().stream().map(FormulaEvent::getContent).anyMatch("+1"::equals));
    assertTrue(expected.getFormulaEvents().stream().map(FormulaEvent::getContent).anyMatch("-1"::equals));
    
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
       aImgidentity,
       formulaMinusOneIdentifierTag,
       relay,
       new BadgeDefinitionGenericEvent(
          aImgidentity,
          new IdentifierTag("UNIT_DOWNVOTE"),
          relay),
       MINUS_ONE_FORMULA);

    List<FormulaEvent> plusOneMinusOneFormulaEvents = List.of(plusOneFormulaEvent, minusOneFormulaEvent);
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgidentity,
       definitionCreatorPublicKey,
       reputationIdentifierTag,
       relay,
       externalIdentityTag,
       plusOneMinusOneFormulaEvents);

    List<FormulaEvent> expectedFormulaEvents = expected.getFormulaEvents();

    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       expected.getGenericEventRecord(),
       addressTag ->
          Stream.of(plusOneFormulaEvent, minusOneFormulaEvent).filter(formulaEvent ->
             formulaEvent.asAddressableEventAddressTag().equals(addressTag)).findFirst().orElseThrow());

    List<FormulaEvent> actualFormulaEvents = badgeDefinitionReputationEvent.getFormulaEvents();

    assertTrue(expectedFormulaEvents.stream()
       .map(AddressableEvent::asAddressableEventAddressTag).toList()
       .containsAll(actualFormulaEvents.stream().map(AddressableEvent::asAddressableEventAddressTag).toList()));
  }

  @Test
  void testValidBadgeDefinitionReputationEvent() {
    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       aImgidentity,
       definitionCreatorPublicKey,
       reputationIdentifierTag,
       relay,
       externalIdentityTag,
       plusOneFormulaEvent);

    assertEquals(externalIdentityTag, badgeDefinitionReputationEvent.getExternalIdentityTag());
  }

  @Test
  void testInequalityEventCopies() throws ParseException {
    BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, relay);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(aImgidentity, formulaPlusOneIdentifierTag, relay, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          reputationIdentifierTag,
          relay,
          externalIdentityTag,
          plusOneFormulaEvent),
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          reputationIdentifierTag,
          relay,
          externalIdentityTag,
          plusOneFormulaEvent));

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          reputationIdentifierTag,
          relay,
          externalIdentityTag,
          plusOneFormulaEvent),
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          reputationIdentifierTag,
          relay,
          externalIdentityTag,
          new FormulaEvent(aImgidentity, formulaPlusOneIdentifierTag, relay, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA)));
  }

  @Test
  void testInequality() throws ParseException {
    BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, relay);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(aImgidentity, formulaPlusOneIdentifierTag, relay, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          reputationIdentifierTag,
          relay,
          externalIdentityTag,
          plusOneFormulaEvent),
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          new IdentifierTag("DIFFERENT_REPUTATION"),
          relay,
          externalIdentityTag,
          plusOneFormulaEvent));

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          reputationIdentifierTag,
          relay,
          externalIdentityTag,
          plusOneFormulaEvent),
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          reputationIdentifierTag,
          relay,
          externalIdentityTag,
          new FormulaEvent(aImgidentity, formulaPlusOneIdentifierTag, relay, badgeDefinitionUpvoteEvent, "+2")));

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgidentity,
          definitionCreatorPublicKey,
          reputationIdentifierTag,
          relay,
          externalIdentityTag,
          plusOneFormulaEvent),
       plusOneFormulaEvent);

    assertNotEquals(badgeDefinitionUpvoteEvent, plusOneFormulaEvent);
  }

  @Test
  void uniqueIdentifierTags() throws ParseException {
    BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, relay);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(aImgidentity, formulaPlusOneIdentifierTag, relay, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);
    List<BaseTag> baseTags = new ArrayList<>();
    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       aImgidentity,
       definitionCreatorPublicKey,
       reputationIdentifierTag,
       relay,
       externalIdentityTag,
       List.of(plusOneFormulaEvent),
       baseTags);

    assertEquals(1, badgeDefinitionReputationEvent.getTypeSpecificTags(IdentifierTag.class).size());
  }

  @Test
  void nonEmptyFormulaEventList() {
    assertTrue(
       assertThrows(
          NostrException.class, () -> new BadgeDefinitionReputationEvent(
             aImgidentity,
             definitionCreatorPublicKey,
             reputationIdentifierTag,
             relay,
             externalIdentityTag,
             List.of(),
             List.of(new IdentifierTag("DIFFERENT_REPUTATION")))
       ).getMessage().contains(BadgeDefinitionReputationEvent.MISSING_FORMULA_EVENTS));
  }

  @Test
  void testDuplicateFormulaEventIdentifierTagsThrowsException() throws ParseException {
    FormulaEvent duplicatePlusOneFormulaEvent = new FormulaEvent(aImgidentity, formulaPlusOneIdentifierTag, relay, badgeDefnUpvoteEvent, "+2");

    assertTrue(
       assertThrows(
          NostrException.class, () ->
             new BadgeDefinitionReputationEvent(
                aImgidentity,
                definitionCreatorPublicKey,
                reputationIdentifierTag,
                relay,
                externalIdentityTag,
                List.of(plusOneFormulaEvent, duplicatePlusOneFormulaEvent))).getMessage().contains(
          BadgeDefinitionReputationEvent.MATCHING_IDENTIFIER_TAGS_FOUND));
  }

  @Test
  void testDifferentFormulaUuidsWithDuplicateFormulaContentsDoNotThrowException() throws ParseException {
    BadgeDefinitionReputationEvent event = new BadgeDefinitionReputationEvent(
       aImgidentity, definitionCreatorPublicKey, reputationIdentifierTag, relay, externalIdentityTag,
       List.of(
          new FormulaEvent(
             aImgidentity, new IdentifierTag(FORMULA_PLUS_ONE), relay,
             new BadgeDefinitionGenericEvent(
                aImgidentity,
                upvoteIdentifierTag,
                relay),
             PLUS_ONE_FORMULA),
          new FormulaEvent(aImgidentity, new IdentifierTag("FORMULA_PLUS_ONE_AGAIN"), relay,
             new BadgeDefinitionGenericEvent(
                aImgidentity,
                new IdentifierTag(UNIT_UPVOTE + "_AGAIN"),
                relay),
             PLUS_ONE_FORMULA)));

    assertEquals(2, event.getFormulaEvents().size());
    assertTrue(event.getFormulaEvents().stream().map(BaseEvent::getContent).allMatch("+1"::equals));
  }

  @Test
  void testGetTypeSpecificTags() {
    assertEquals(1,
       new BadgeDefinitionGenericEvent(
          aImgidentity,
          reputationIdentifierTag, relay).getTypeSpecificTags(IdentifierTag.class).size());
  }
}
