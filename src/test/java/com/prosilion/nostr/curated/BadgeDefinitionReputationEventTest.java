package com.prosilion.nostr.curated;

import com.prosilion.nostr.EventTestFixtures;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.AddressableEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BaseEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.curated.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.curated.CuratedFormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeDefinitionReputationEventTest extends EventTestFixtures {
  public static final Relay relay = new Relay("ws://localhost:5555");

  private final BadgeDefinitionGenericEvent badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, relay);
  private final BadgeDefinitionGenericEvent badgeDefnDownvoteEvent = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag, relay);

  private final CuratedFormulaEvent plusOneFormulaEvent =
     new CuratedFormulaEvent(
        aImgIdentity,
        new FormulaEvent(
           formulaCreator,
           formulaUpvoteIdentifierTag,
           badgeDefnUpvoteEvent,
           PLUS_ONE_FORMULA,
           relay),
        new ReferenceTag(relayArgUrl),
        relayArgRelay);

  private final CuratedFormulaEvent minusOneFormulaEvent =
     new CuratedFormulaEvent(
        aImgIdentity,
        new FormulaEvent(
           formulaCreator,
           formulaDownvoteIdentifierTag,
           badgeDefnDownvoteEvent,
           MINUS_ONE_FORMULA,
           relay),
        new ReferenceTag(relayArgUrl),
        relayArgRelay);

  public BadgeDefinitionReputationEventTest() {
  }

  @Test
  void testValidBadgeDefinitionReputationEventWithPlusOneFormulaEvent() {
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       repDefnCreator.getPublicKey(),
       reputationIdentifierTag,
       EXTERNAL_IDENTITY_TAG, relay,
       plusOneFormulaEvent);

    IdentifierTag expectedIdentifierTag = AbstractSetsEvent.hashedAddressTag(badgeDefnUpvoteEvent.asAddressableEventAddressTag());
    assertEquals(expectedIdentifierTag, plusOneFormulaEvent.getIdentifierTag());
    assertEquals(expectedIdentifierTag, expected.getTypeSpecificTags(AddressTag.class).getFirst().getIdentifierTag());

    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       expected.getGenericEventRecord(),
       addressTag -> plusOneFormulaEvent);

    assertEquals(
       expected.getCuratedFormulaEvents(),
       badgeDefinitionReputationEvent.getCuratedFormulaEvents());
  }

  @Test
  void testValidBadgeDefinitionReputationEventWithPlusOneMinusOneFormulaEvent() {
    List<CuratedFormulaEvent> formulaEvents = List.of(plusOneFormulaEvent, minusOneFormulaEvent);
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       repDefnCreator.getPublicKey(),
       reputationIdentifierTag,
       EXTERNAL_IDENTITY_TAG, relay,
       formulaEvents);

    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       expected.getGenericEventRecord(),
       addressTag ->
          formulaEvents.stream().filter(formulaEvent ->
                addressTag.equals(formulaEvent.asAddressableEventAddressTag()))
             .findFirst().orElseThrow());

    assertEquals(
       expected.getCuratedFormulaEvents(),
       badgeDefinitionReputationEvent.getCuratedFormulaEvents());
  }

  @Test
  void testValidBadgeDefinitionReputationEventWithPlusOneMinusOneFormulaEventUsingVarArgs() {
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       repDefnCreator.getPublicKey(),
       reputationIdentifierTag,
       EXTERNAL_IDENTITY_TAG, relay,
       plusOneFormulaEvent, minusOneFormulaEvent);

    List<CuratedFormulaEvent> formulaEvents = List.of(plusOneFormulaEvent, minusOneFormulaEvent);
    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       expected.getGenericEventRecord(),
       addressTag ->
          formulaEvents.stream().filter(formulaEvent ->
                addressTag.equals(formulaEvent.asAddressableEventAddressTag()))
             .findFirst().orElseThrow());

    assertEquals(2, expected.getCuratedFormulaEvents().size());
    assertEquals(2, badgeDefinitionReputationEvent.getCuratedFormulaEvents().size());

    assertTrue(expected.getCuratedFormulaEvents().stream().map(CuratedFormulaEvent::getContent).anyMatch("+1"::equals));
    assertTrue(expected.getCuratedFormulaEvents().stream().map(CuratedFormulaEvent::getContent).anyMatch("-1"::equals));

    assertEquals(
       expected.getCuratedFormulaEvents(),
       badgeDefinitionReputationEvent.getCuratedFormulaEvents());
  }

  @Test
  void testValidBadgeDefinitionReputationEventWithPopulatedFormulaEvents() {
    final String FORMULA_MINUS_ONE = "FORMULA_PLUS_ONE";
    IdentifierTag formulaMinusOneIdentifierTag = new IdentifierTag(FORMULA_MINUS_ONE);
    final String MINUS_ONE_FORMULA = "-1";

    CuratedFormulaEvent minusOneFormulaEvent = new CuratedFormulaEvent(
       aImgIdentity,
       new FormulaEvent(
          formulaCreator,
          formulaMinusOneIdentifierTag,
          new BadgeDefinitionGenericEvent(
             aImgIdentity,
             downvoteIdentifierTag,
             relay),
          MINUS_ONE_FORMULA,
          relay),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    List<CuratedFormulaEvent> plusOneMinusOneFormulaEvents = List.of(plusOneFormulaEvent, minusOneFormulaEvent);
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       repDefnCreator.getPublicKey(),
       reputationIdentifierTag,
       EXTERNAL_IDENTITY_TAG, relay,
       plusOneMinusOneFormulaEvents);

    List<CuratedFormulaEvent> expectedFormulaEvents = expected.getCuratedFormulaEvents();

    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       expected.getGenericEventRecord(),
       addressTag ->
          Stream.of(plusOneFormulaEvent, minusOneFormulaEvent).filter(formulaEvent ->
             formulaEvent.asAddressableEventAddressTag().equals(addressTag)).findFirst().orElseThrow());

    List<CuratedFormulaEvent> actualFormulaEvents = badgeDefinitionReputationEvent.getCuratedFormulaEvents();

    assertTrue(expectedFormulaEvents.stream()
       .map(AddressableEvent::asAddressableEventAddressTag).toList()
       .containsAll(actualFormulaEvents.stream().map(AddressableEvent::asAddressableEventAddressTag).toList()));
  }

  @Test
  void testValidBadgeDefinitionReputationEvent() {
    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       repDefnCreator.getPublicKey(),
       reputationIdentifierTag,
       EXTERNAL_IDENTITY_TAG, relay,
       plusOneFormulaEvent);

    assertEquals(EXTERNAL_IDENTITY_TAG, badgeDefinitionReputationEvent.getExternalIdentityTag());
  }

  @Test
  void testInequalityEventCopies() {
    BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionGenericEvent(aImgIdentity, upvoteIdentifierTag, relay);
    CuratedFormulaEvent plusOneFormulaEvent = new CuratedFormulaEvent(
       aImgIdentity,
       new FormulaEvent(
          formulaCreator,
          formulaUpvoteIdentifierTag,
          badgeDefinitionUpvoteEvent,
          PLUS_ONE_FORMULA,
          relay),
       new ReferenceTag(relayArgUrl),
       relayArgRelay);

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relay,
          plusOneFormulaEvent),
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relay,
          plusOneFormulaEvent));

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relay,
          plusOneFormulaEvent),
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relay,
          new CuratedFormulaEvent(
             aImgIdentity,
             new FormulaEvent(
                formulaCreator,
                formulaUpvoteIdentifierTag,
                badgeDefinitionUpvoteEvent,
                PLUS_ONE_FORMULA,
                relay),
             new ReferenceTag(relayArgUrl),
             relayArgRelay)));
  }

  @Test
  void testInequality() {
    BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionGenericEvent(aImgIdentity, upvoteIdentifierTag, relay);
    CuratedFormulaEvent plusOneFormulaEvent =
       new CuratedFormulaEvent(
          aImgIdentity,
          new FormulaEvent(
             formulaCreator,
             formulaUpvoteIdentifierTag,
             badgeDefinitionUpvoteEvent,
             PLUS_ONE_FORMULA,
             relay),
          new ReferenceTag(relayArgUrl),
          relayArgRelay);

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relay,
          plusOneFormulaEvent),
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          new IdentifierTag("DIFFERENT_REPUTATION"),
          EXTERNAL_IDENTITY_TAG, relay,
          plusOneFormulaEvent));

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relay,
          plusOneFormulaEvent),
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relay,
          new CuratedFormulaEvent(
             aImgIdentity,
             new FormulaEvent(
                formulaCreator,
                formulaUpvoteIdentifierTag,
                badgeDefinitionUpvoteEvent,
                "+2",
                relay),
             new ReferenceTag(relayArgUrl),
             relayArgRelay)));

    assertNotEquals(
       new BadgeDefinitionReputationEvent(
          aImgIdentity,
          repDefnCreator.getPublicKey(),
          reputationIdentifierTag,
          EXTERNAL_IDENTITY_TAG, relay,
          plusOneFormulaEvent),
       plusOneFormulaEvent);

    assertNotEquals(badgeDefinitionUpvoteEvent, plusOneFormulaEvent);
  }

  @Test
  void uniqueIdentifierTags() {
    BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionGenericEvent(aImgIdentity, upvoteIdentifierTag, relay);
    CuratedFormulaEvent plusOneFormulaEvent =
       new CuratedFormulaEvent(aImgIdentity,
          new FormulaEvent(
             formulaCreator,
             formulaUpvoteIdentifierTag,
             badgeDefinitionUpvoteEvent,
             PLUS_ONE_FORMULA,
             relay),
          new ReferenceTag(relayArgUrl),
          relayArgRelay);
    List<BaseTag> baseTags = new ArrayList<>();
    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       upvoteDefnCreator.getPublicKey(),
       reputationIdentifierTag,
       EXTERNAL_IDENTITY_TAG, baseTags, relay, List.of(plusOneFormulaEvent)
    );

    assertEquals(1, badgeDefinitionReputationEvent.getTypeSpecificTags(IdentifierTag.class).size());
  }

  @Test
  void nonEmptyFormulaEventList() {
    assertTrue(
       assertThrows(
          NostrException.class, () -> new BadgeDefinitionReputationEvent(
             aImgIdentity,
             upvoteDefnCreator.getPublicKey(),
             reputationIdentifierTag,
             EXTERNAL_IDENTITY_TAG, List.of(new IdentifierTag("DIFFERENT_REPUTATION")), relay, List.of()
          )
       ).getMessage().contains(BadgeDefinitionReputationEvent.MISSING_FORMULA_EVENTS));
  }

  @Test
  void testDuplicateFormulaEventIdentifierTagsThrowsException() {
    CuratedFormulaEvent duplicatePlusOneFormulaEvent =
       new CuratedFormulaEvent(
          aImgIdentity,
          new FormulaEvent(
             formulaCreator,
             formulaUpvoteIdentifierTag,
             badgeDefnUpvoteEvent,
             "+2",
             relay),
          new ReferenceTag(relayArgUrl),
          relayArgRelay);

    assertTrue(
       assertThrows(
          NostrException.class, () ->
             new BadgeDefinitionReputationEvent(
                aImgIdentity,
                upvoteDefnCreator.getPublicKey(),
                reputationIdentifierTag,
                EXTERNAL_IDENTITY_TAG, relay,
                List.of(plusOneFormulaEvent, duplicatePlusOneFormulaEvent))).getMessage().contains(
          BadgeDefinitionReputationEvent.MATCHING_IDENTIFIER_TAGS_FOUND));
  }

  @Test
  void testDifferentFormulaUuidsWithDuplicateFormulaContentsDoNotThrowException() {
    BadgeDefinitionReputationEvent event = new BadgeDefinitionReputationEvent(
       aImgIdentity, upvoteDefnCreator.getPublicKey(), reputationIdentifierTag, EXTERNAL_IDENTITY_TAG, relay,
       List.of(
          new CuratedFormulaEvent(
             aImgIdentity,
             new FormulaEvent(
                formulaCreator,
                formulaUpvoteIdentifierTag,
                new BadgeDefinitionGenericEvent(
                   aImgIdentity,
                   upvoteIdentifierTag,
                   relay),
                PLUS_ONE_FORMULA,
                relay),
             new ReferenceTag(relayArgUrl),
             relayArgRelay),
          new CuratedFormulaEvent(
             aImgIdentity,
             new FormulaEvent(
                formulaCreator,
                new IdentifierTag(FORMULA_UNIT_UPVOTE + "_AGAIN"),
                new BadgeDefinitionGenericEvent(
                   aImgIdentity,
                   new IdentifierTag(AWARD_UNIT_UPVOTE + "_AGAIN"),
                   relay),
                PLUS_ONE_FORMULA,
                relay),
             new ReferenceTag(relayArgUrl),
             relayArgRelay)));

    assertEquals(2, event.getCuratedFormulaEvents().size());
    assertTrue(event.getCuratedFormulaEvents().stream().map(BaseEvent::getContent).allMatch("+1"::equals));
  }

  @Test
  void testAddressTagCount() {
    BadgeDefinitionReputationEvent expected = new BadgeDefinitionReputationEvent(
       aImgIdentity,
       repDefnCreator.getPublicKey(),
       reputationIdentifierTag,
       EXTERNAL_IDENTITY_TAG, relay,
       plusOneFormulaEvent);

    assertEquals(1, expected.getCuratedFormulaEvents().size());
  }

  @Test
  void testGetTypeSpecificTags() {
    assertEquals(1,
       new BadgeDefinitionGenericEvent(
          aImgIdentity,
          reputationIdentifierTag, relay).getTypeSpecificTags(IdentifierTag.class).size());
  }
}
