package com.prosilion.nostr;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FormulaEventTest {
  public final static String UNIT_REPUTATION = "UNIT_REPUTATION";
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
  void consoleLogTest() throws ParseException {
    String PLUS_ONE_FORMULA = "+1";
    String MINUS_ONE_FORMULA = "-1";

    Identity identity = Identity.generateRandomIdentity();

    assertEquals("UNIT_REPUTATION == (previous)UNIT_REPUTATION +1(UNIT_UPVOTE) -1(UNIT_DOWNVOTE)",
        new BadgeDefinitionReputationEvent(
            identity,
            new IdentifierTag(
                UNIT_REPUTATION),
            List.of(
                new FormulaEvent(
                    identity,
                    awardUpvoteEvent,
                    PLUS_ONE_FORMULA),
                new FormulaEvent(
                    identity,
                    awardDownvoteEvent,
                    MINUS_ONE_FORMULA))).getContent());

    String UNIT_UPVOTE_UNIQUE = "UNIT_UPVOTE_UNIQUE";
    IdentifierTag upvoteUniqueIdentifierTag = new IdentifierTag(UNIT_UPVOTE_UNIQUE);
    BadgeDefinitionAwardEvent awardUniqueUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteUniqueIdentifierTag);

    assertEquals("UNIT_REPUTATION == (previous)UNIT_REPUTATION +1(UNIT_UPVOTE) +1(UNIT_UPVOTE_UNIQUE)",
        new BadgeDefinitionReputationEvent(
            identity,
            new IdentifierTag(
                UNIT_REPUTATION),
            List.of(
                new FormulaEvent(
                    identity,
                    awardUpvoteEvent,
                    PLUS_ONE_FORMULA),
                new FormulaEvent(
                    identity,
                    awardUniqueUpvoteEvent,
                    PLUS_ONE_FORMULA))).getContent());
    
    assertThrows(NostrException.class, () ->
        new BadgeDefinitionReputationEvent(
            identity,
            new IdentifierTag(
                UNIT_REPUTATION),
            List.of(
                new FormulaEvent(
                    identity,
                    awardUpvoteEvent,
                    PLUS_ONE_FORMULA),
                new FormulaEvent(
                    identity,
                    awardUpvoteEvent,
                    PLUS_ONE_FORMULA))));
  }

  private void validateReturnedFormula(String formula) throws ParseException {
    new Expression(
        String.format("%s %s", "validate", formula)
    ).validate();
  }
}
