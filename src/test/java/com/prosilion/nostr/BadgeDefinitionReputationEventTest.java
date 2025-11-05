package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeDefinitionReputationEventTest {
  public final static String REPUTATION = "REPUTATION";
  public final static String UNIT_UPVOTE = "UNIT_UPVOTE";
  public static final String PLUS_ONE_FORMULA = "+1";

  public final IdentifierTag reputationIdentifierTag = new IdentifierTag(REPUTATION);
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  public final Identity identity = Identity.generateRandomIdentity();

  @Test
  void testValidBadgeDefinitionReputationEvent() throws ParseException {
    BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);
    new BadgeDefinitionReputationEvent(
        identity,
        reputationIdentifierTag,
        plusOneFormulaEvent);
  }

  @Test
  void testEquality() {
    assertEquals(
        new BadgeDefinitionAwardEvent(
            identity,
            reputationIdentifierTag),
        new BadgeDefinitionAwardEvent(
            identity,
            reputationIdentifierTag));
  }

  @Test
  void testGetTypeSpecificTags() {
    assertEquals(1,
        new BadgeDefinitionAwardEvent(
            identity,
            reputationIdentifierTag).getTypeSpecificTags(IdentifierTag.class).size());
  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
