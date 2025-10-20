package com.prosilion.nostr.event;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public class BadgeDefinitionReputationFormulaEvent extends ArbitraryCustomAppDataEvent {
  public BadgeDefinitionReputationFormulaEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull String formula) throws NostrException, ParseException {
    this(
        identity,
        identifierTag,
        List.of(),
        validate(formula));
  }

  public BadgeDefinitionReputationFormulaEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<BaseTag> baseTags,
      @NonNull String formula) throws NostrException, ParseException {
    super(
        identity,
        identifierTag,
        baseTags,
        validate(formula));
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
