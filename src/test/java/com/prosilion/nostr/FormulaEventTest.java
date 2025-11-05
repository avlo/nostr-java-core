package com.prosilion.nostr;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormulaEventTest {
  public final static String UNIT_UPVOTE = "UNIT_UPVOTE";
  public final static String UNIT_DOWNVOTE = "UNIT_DOWNVOTE";

  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_DOWNVOTE);

  public final Identity identity = Identity.generateRandomIdentity();
  public final BadgeDefinitionAwardEvent awardUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag);
  public final BadgeDefinitionAwardEvent awardDownvoteEvent = new BadgeDefinitionAwardEvent(identity, downvoteIdentifierTag);

  @Test
  public void formulaValidationTestUnitAdd() throws ParseException {
    String formula = "+1";
    new Expression(
        String.format("%s %s", "validate", formula)
    ).validate();
  }

  @Test
  public void formulaValidationTestUnitSubtract() throws ParseException {
    String formula = "-1";
    new Expression(
        String.format("%s %s", "validate", formula)
    ).validate();
  }

  @Test
  public void formulaValidationFailDueToNonFormulaTest() {
    String formula = "b";
    assertThrows(ParseException.class, () -> new Expression(
        String.format("%s %s", "validate", formula)
    ).validate());
  }

  @Test
  public void formulaValidationFailDueToMissingOperatorTest() {
    String formula = "1";
    assertThrows(ParseException.class, () -> new Expression(
        String.format("%s %s", "validate", formula)
    ).validate());
  }

  @Test
  public void testEquality() throws ParseException {
    FormulaEvent upvoteFormulaEvent = new FormulaEvent(identity, awardUpvoteEvent, "+1");
    FormulaEvent upvoteFormulaEventDuplicate = new FormulaEvent(identity, awardUpvoteEvent, "+1");

    FormulaEvent upvoteFormulaEventTriplicate = new FormulaEvent(
        identity,
        new BadgeDefinitionAwardEvent(
            identity,
            upvoteIdentifierTag),
        "+1");

    assertEquals(upvoteFormulaEvent, upvoteFormulaEventTriplicate);
    assertEquals(upvoteFormulaEventDuplicate, upvoteFormulaEventTriplicate);
  }

  @Test
  void testInequality() throws ParseException {
    FormulaEvent formulaEvent = new FormulaEvent(Identity.generateRandomIdentity(), awardUpvoteEvent, "+1");
    FormulaEvent differentUpvoteFormula = new FormulaEvent(identity, awardUpvoteEvent, "+2");
    assertNotEquals(formulaEvent, differentUpvoteFormula);
    assertNotEquals(formulaEvent, awardDownvoteEvent);
  }

  @Test
  public void arbitraryCustomAppDataFormulaEventTest() throws ParseException {
    validateReturnedFormula(
        new FormulaEvent(
            identity,
            awardUpvoteEvent,
            "+1").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            awardDownvoteEvent,
            "-1").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            awardDownvoteEvent,
            "+-1").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            awardDownvoteEvent,
            "--1").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            awardUpvoteEvent,
            "+(-1)").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            awardDownvoteEvent,
            "-(-1)").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            awardUpvoteEvent,
            "-(+1)").getFormula());

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        awardUpvoteEvent,
        "a"));

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        awardUpvoteEvent,
        ""));

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        awardUpvoteEvent,
        " "));

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        awardUpvoteEvent,
        " "));

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        awardUpvoteEvent,
        "1"));
  }

  @Test
  void testBlankFormulaEvent() {
    assertTrue(
        assertThrows(
            ParseException.class, () ->
                new FormulaEvent(
                    identity,
                    awardUpvoteEvent,
                    ""))
            .getMessage().contains("supplied formula is blank"));
  }

  private void validateReturnedFormula(String formula) throws ParseException {
    new Expression(
        String.format("%s %s", "validate", formula)
    ).validate();
  }
}
