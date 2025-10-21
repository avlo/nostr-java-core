package com.prosilion.nostr;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import org.junit.jupiter.api.Test;

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

  private void validateReturnedFormula(String formula) throws ParseException {
    new Expression(
        String.format("%s %s", "validate", formula)
    ).validate();
  }
}
