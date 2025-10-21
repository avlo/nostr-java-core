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
    Identity identity = Identity.generateRandomIdentity();
    IdentifierTag identifierTag = new IdentifierTag("BadgeDefinitionReputation-UUID");

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            identifierTag,
            "+1").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            identifierTag,
            "-1").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            identifierTag,
            "+-1").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            identifierTag,
            "--1").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            identifierTag,
            "+(-1)").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            identifierTag,
            "-(-1)").getFormula());

    validateReturnedFormula(
        new FormulaEvent(
            identity,
            identifierTag,
            "-(+1)").getFormula());
    
    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        identifierTag,
        "a"));

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        identifierTag,
        ""));

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        identifierTag,
        " "));

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        identifierTag,
        " "));

    assertThrows(ParseException.class, () -> new FormulaEvent(
        identity,
        identifierTag,
        "1"));
  }

  @Test
  void consoleLogTest() throws ParseException {
    String UNIT_REPUTATION = "UNIT_REPUTATION";
    String UNIT_UPVOTE = "UNIT_UPVOTE";
    String UNIT_DOWNVOTE = "UNIT_DOWNVOTE";
    
    IdentifierTag UPVOTE_IDENTIFIER_TAG = new IdentifierTag(UNIT_UPVOTE);
    IdentifierTag DOWNVOTE_IDENTIFIER_TAG = new IdentifierTag(UNIT_DOWNVOTE);

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
                UPVOTE_IDENTIFIER_TAG,
                PLUS_ONE_FORMULA),
            new FormulaEvent(
                identity,
                DOWNVOTE_IDENTIFIER_TAG,
                MINUS_ONE_FORMULA))).getContent());

    assertEquals("UNIT_REPUTATION == (previous)UNIT_REPUTATION +1(UNIT_UPVOTE) +1(UNIT_UPVOTE)",
        new BadgeDefinitionReputationEvent(
        identity,
        new IdentifierTag(
            UNIT_REPUTATION),
        List.of(
            new FormulaEvent(
                identity,
                UPVOTE_IDENTIFIER_TAG,
                PLUS_ONE_FORMULA),
            new FormulaEvent(
                identity,
                UPVOTE_IDENTIFIER_TAG,
                PLUS_ONE_FORMULA))).getContent());
  }
  
  private void validateReturnedFormula(String formula) throws ParseException {
    new Expression(
        String.format("%s %s", "validate", formula)
    ).validate();
  }
}
