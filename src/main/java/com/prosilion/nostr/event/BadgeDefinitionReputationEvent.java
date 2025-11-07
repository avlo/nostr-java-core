package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class BadgeDefinitionReputationEvent extends BadgeDefinitionAwardEvent {
  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull FormulaEvent formulaEvent) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(formulaEvent));
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<FormulaEvent> formulaEvents) throws NostrException {
    this(
        identity,
        identifierTag,
        formulaEvents,
        List.of());
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<FormulaEvent> formulaEvents,
      @NonNull List<BaseTag> baseTags) throws NostrException {
    super(
        identity,
        identifierTag,
        Stream.concat(
            formulaEvents.stream().map(FormulaEvent::getId).map(EventTag::new),
            baseTags.stream()).toList(),
        defaultContentFromFormulaOperators(identifierTag, formulaEvents));
  }

  public BadgeDefinitionReputationEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }

  private static String defaultContentFromFormulaOperators(IdentifierTag identifierTag, List<FormulaEvent> formulaEvents) {
    List<IdentifierTag> identifierTags = formulaEvents.stream().map(FormulaEvent::getIdentifierTag).toList();

    Optional.of(
            identifierTags.stream().distinct().count() != formulaEvents.size())
        .filter(Boolean::booleanValue)
        .map(bool -> {
          throw new NostrException(String.format("Matching formula events found in %s", identifierTags));
        });

    return String.format("%s == (previous)%s%s", identifierTag.getUuid(), identifierTag.getUuid(), operatorFormatDisplayIterator(formulaEvents));
  }

  private static String operatorFormatDisplayIterator(List<FormulaEvent> formulaEvents) {
    StringBuilder sb = new StringBuilder();
    formulaEvents.forEach(formula -> sb
        .append(" ")
        .append(formula.getFormula())
        .append("(")
        .append(formula.getIdentifierTag().getUuid())
        .append(")"));
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (!Objects.equals(this.getClass(), obj.getClass()))
      return false;

    BadgeDefinitionReputationEvent that = (BadgeDefinitionReputationEvent) obj;
    return super.equals(that);
  }
}
