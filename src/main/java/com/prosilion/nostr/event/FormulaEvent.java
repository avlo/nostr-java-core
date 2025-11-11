package com.prosilion.nostr.event;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public class FormulaEvent extends TextNoteEvent implements EventTagsMappedEventsIF {
  @Getter
  private final BadgeDefinitionAwardEvent badgeDefinitionAwardEvent;

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
        Stream.concat(
            Stream.of(new EventTag(badgeDefinitionAwardEvent.getId())),
            baseTags.stream()).toList(),
        validate(formula));
    this.badgeDefinitionAwardEvent = badgeDefinitionAwardEvent;
  }

  public FormulaEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<EventTag, BadgeDefinitionAwardEvent> fxn) {
    super(genericEventRecord);
    this.badgeDefinitionAwardEvent = mapEventTagsToEvents(this, fxn).getFirst();
  }

  public String getFormula() {
    return super.getContent();
  }

  private static String validate(String formula) throws ParseException {
    if (StringUtils.isBlank(formula))
      throw new ParseException(formula, "supplied formula is blank");
//    TODO: store expression in global expression map
    new Expression(
        String.format("%s %s", "validate", formula)).validate();
    return formula;
  }
}
