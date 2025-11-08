package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void testInequalityEventCopies() throws ParseException {
    BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            plusOneFormulaEvent),
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            plusOneFormulaEvent));

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            plusOneFormulaEvent),
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            new FormulaEvent(identity, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA)));
  }

  @Test
  void testInequality() throws ParseException {
    BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            plusOneFormulaEvent),
        new BadgeDefinitionReputationEvent(
            identity,
            new IdentifierTag("DIFFERENT_REPUTATION"),
            plusOneFormulaEvent));

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            plusOneFormulaEvent),
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            new FormulaEvent(identity, badgeDefinitionUpvoteEvent, "+2")));

    assertNotEquals(
        new BadgeDefinitionReputationEvent(
            identity,
            reputationIdentifierTag,
            plusOneFormulaEvent),
        plusOneFormulaEvent);

    assertNotEquals(badgeDefinitionUpvoteEvent, plusOneFormulaEvent);
  }

  @Test
  void nonSingularIdentifierTags() throws ParseException {
    BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag);
    FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, badgeDefinitionUpvoteEvent, PLUS_ONE_FORMULA);
    List<BaseTag> baseTags = new ArrayList<>();
    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
    String message = assertThrows(
        AssertionError.class, () ->
            new BadgeDefinitionReputationEvent(
                identity,
                reputationIdentifierTag,
                List.of(plusOneFormulaEvent),
                baseTags)).getMessage();
    assertTrue(
        message.contains(
            "List<BaseTag> should contain [1] IdentifierTag but instead has [2]"
        ));
  }

  @Test
  void testGetTypeSpecificTags() {
    assertEquals(1,
        new BadgeDefinitionAwardEvent(
            identity,
            reputationIdentifierTag).getTypeSpecificTags(IdentifierTag.class).size());
  }
}
