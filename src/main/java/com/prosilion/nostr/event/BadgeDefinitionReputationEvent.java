package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeDefinitionReputationEvent extends BadgeDefinitionAwardEvent {
  @JsonIgnore @Getter List<FormulaEvent> formulaEvents;

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
        List.of(),
        defaultContentFromFormulaOperators(identifierTag, formulaEvents));
  }

  private static String defaultContentFromFormulaOperators(IdentifierTag identifierTag, List<FormulaEvent> formulaEvents) {
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

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull FormulaEvent formulaEvent,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(formulaEvent),
        List.of(),
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<FormulaEvent> formulaEvent,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        formulaEvent,
        List.of(),
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull FormulaEvent formulaEvent,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(formulaEvent),
        baseTags,
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<FormulaEvent> formulaEvents,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        identifierTag,
        Stream.concat(
            formulaEvents.stream()
                .map(FormulaEvent::getId)
                .map(EventTag::new),
            baseTags.stream()).toList(),
        content);
    this.formulaEvents = formulaEvents;
  }

  @JsonIgnore
  public List<EventTag> getEventTags() {
    return formulaEvents.stream()
        .map(FormulaEvent::getId)
        .map(EventTag::new).toList();
  }

  public record BadgeDefinitionReputationEventFormulaMapUtil(
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
    @JsonIgnore
    public Map<BadgeDefinitionReputationEvent, List<String>> asFormulaUuidMap() {
      return fxn.apply(badgeDefinitionReputationEvent);
    }

    @JsonIgnore
    public Map<BadgeDefinitionReputationEvent, List<String>> asFormulaUuidMapWithAccumulator(@NonNull BadgeDefinitionReputationEventFormulaMapUtil other) {
      asFormulaUuidMapWithAccumulator(other.asFormulaUuidMap());
      return other.asFormulaUuidMap();
    }

    @JsonIgnore
    public Map<BadgeDefinitionReputationEvent, List<String>> asFormulaUuidMapWithAccumulator(@NonNull Map<BadgeDefinitionReputationEvent, List<String>> other) {
      other.putIfAbsent(badgeDefinitionReputationEvent, getUuids(badgeDefinitionReputationEvent));
      return other;
    }

    private static final Function<BadgeDefinitionReputationEvent, Map<BadgeDefinitionReputationEvent, List<String>>> fxn =
        event -> Map.of(event, getUuids(event));

    private static List<String> getUuids(BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
      return badgeDefinitionReputationEvent.getFormulaEvents().stream()
          .map(FormulaEvent::getIdentifierTag)
          .map(IdentifierTag::getUuid)
          .toList();
    }
  }
}
