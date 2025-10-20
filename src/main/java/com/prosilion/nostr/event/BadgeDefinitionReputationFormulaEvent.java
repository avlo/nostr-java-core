package com.prosilion.nostr.event;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public class BadgeDefinitionReputationFormulaEvent extends ArbitraryCustomAppDataEvent {
  @JsonIgnore @Getter IdentifierTag identifierTag;

  public BadgeDefinitionReputationFormulaEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull String formula) throws NostrException, ParseException {
    super(
        identity,
        identifierTag,
        validate(formula));
    this.identifierTag = identifierTag;
  }

  public String getFormula() {
    return super.getContent();
  }

  private static String validate(String formula) throws ParseException {
    if (StringUtils.isBlank(formula))
      throw new ParseException(formula, "supplied formula is blank");
    new Expression(
        String.format("%s %s", "validate", formula)).validate();
    return formula;
  }
}
