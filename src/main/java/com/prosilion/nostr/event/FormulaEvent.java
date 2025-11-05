package com.prosilion.nostr.event;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public class FormulaEvent extends ArbitraryCustomAppDataEvent {
  public FormulaEvent(
      @NonNull Identity identity,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionAwardEvent,
      @NonNull String formula) throws NostrException, ParseException {
    this(
        identity,
        badgeDefinitionAwardEvent,
        List.of(),
        formula);
  }

  public FormulaEvent(
      @NonNull Identity identity,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionAwardEvent,
      @NonNull List<BaseTag> baseTags,
      @NonNull String formula) throws NostrException, ParseException {
    super(
        identity,
        badgeDefinitionAwardEvent.getIdentifierTag(),
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
