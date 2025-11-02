package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeDefinitionReputationEventTest {
  public final static String REPUTATION = "REPUTATION";
  public final static String UNIT_UPVOTE = "UNIT_UPVOTE";

  public final IdentifierTag definitionIdentifierTag = new IdentifierTag(REPUTATION);
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  public final Identity identity = Identity.generateRandomIdentity();

  @Test
  void testValidBadgeDefinitionReputationEvent() {
    new BadgeDefinitionReputationEvent(
        identity,
        definitionIdentifierTag,
        new EventTag(generateRandomHex64String()));

    new BadgeDefinitionReputationEvent(
        identity,
        definitionIdentifierTag,
        List.of(new EventTag(generateRandomHex64String())));

    new BadgeDefinitionReputationEvent(
        identity,
        definitionIdentifierTag,
        List.of(new EventTag(generateRandomHex64String())),
        "content");

    new BadgeDefinitionReputationEvent(
        identity,
        definitionIdentifierTag,
        new EventTag(generateRandomHex64String()),
        List.of(),
        "content");

    new BadgeDefinitionReputationEvent(
        identity,
        definitionIdentifierTag,
        List.of(new EventTag(generateRandomHex64String())),
        List.of(),
        "content");
  }

  @Test
  void testInvalidBadgeDefinitionReputationEvent() {
    assertThrows(AssertionError.class, () -> new BadgeDefinitionReputationEvent(
        identity,
        definitionIdentifierTag,
        List.of(new EventTag(generateRandomHex64String())),
        List.of(upvoteIdentifierTag),
        "content"));

    try {
      new BadgeDefinitionReputationEvent(
          identity,
          definitionIdentifierTag,
          List.of(new EventTag(generateRandomHex64String())),
          List.of(upvoteIdentifierTag),
          "content");
    } catch (AssertionError e) {
      assertTrue(e.getMessage().contains("UniqueIdentifierTagEvent List<BaseTag> should contain [1] IdentifierTag but instead has [2]"));
    }
  }

  @Test
  void testEquality() {
    assertEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            definitionIdentifierTag,
            List.of(new EventTag(generateRandomHex64String())),
            List.of(),
            "content"),
        new BadgeDefinitionReputationEvent(
            identity,
            definitionIdentifierTag,
            List.of(new EventTag(generateRandomHex64String())),
            List.of(),
            "content"));

    assertEquals(
        new BadgeDefinitionAwardEvent(
            identity,
            definitionIdentifierTag),
        new BadgeDefinitionAwardEvent(
            identity,
            definitionIdentifierTag));

    assertEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            definitionIdentifierTag,
            List.of(),
            "content"),
        new BadgeDefinitionAwardEvent(
            identity,
            definitionIdentifierTag,
            List.of(),
            "content"));

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            definitionIdentifierTag,
            List.of(),
            "content"),
        new BadgeDefinitionAwardEvent(
            identity,
            upvoteIdentifierTag,
            List.of(),
            "content"));

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            definitionIdentifierTag,
            List.of(),
            "content"),
        new BadgeDefinitionAwardEvent(
            Identity.generateRandomIdentity(),
            definitionIdentifierTag,
            List.of(),
            "content"));
  }

  @Test
  void testIsAssignableFromFalse() throws ParseException {
    assertFalse(
        new BadgeDefinitionReputationEvent(
            identity,
            definitionIdentifierTag,
            List.of(),
            "content")
        .equals(
             new FormulaEvent(
               identity,
               new BadgeDefinitionAwardEvent(
                  identity,
                  definitionIdentifierTag),
       "+1")));
  }

  @Test
  void testGetTypeSpecificTags() {
    assertEquals(1,
        new BadgeDefinitionAwardEvent(
            identity,
            definitionIdentifierTag).getTypeSpecificTags(IdentifierTag.class).size());
  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
