package com.prosilion.nostr;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadgeReputationDefinitionEventTest {

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
  public void testBadgeReputationDefinitionEvent() throws ParseException {

  }
}
