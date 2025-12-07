package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;

public class BadgeDefinitionReputationEvent extends BadgeDefinitionAwardEvent implements AddressableTagsMappedEventsIF {
  public static final String MATCHING_IDENTIFIER_TAGS_FOUND = "Formula events containing illegal matching identifier tags found:";
  public static final String CONCAT = Strings.concat(MATCHING_IDENTIFIER_TAGS_FOUND, " [%s]");
  @Getter
  private final List<FormulaEvent> formulaEvents;

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull FormulaEvent formulaEvent) throws NostrException {
    this(
        identity,
        identifierTag,
        relay,
        externalIdentityTag,
        List.of(formulaEvent));
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull List<FormulaEvent> formulaEvents) throws NostrException {
    this(
        identity,
        identifierTag,
        relay,
        externalIdentityTag,
        formulaEvents,
        List.of());
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull List<FormulaEvent> formulaEvents,
      @NonNull List<BaseTag> baseTags) throws NostrException {
    super(
        identity,
        identifierTag,
        relay,
        Stream.concat(
            formulaEvents.stream()
                .map(FormulaEvent::getContainedEventsAsAddressTags)
                .flatMap(List::stream),
            Stream.concat(
                Stream.of(externalIdentityTag),
                baseTags.stream())).toList(),
        defaultContentFromFormulaOperators(identifierTag, formulaEvents));
    this.formulaEvents = formulaEvents;
  }

  public BadgeDefinitionReputationEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, FormulaEvent> eventTagFormulaEventFunction) {
    super(genericEventRecord);
    this.formulaEvents = mapAddressableTagsToEvents(this, eventTagFormulaEventFunction);
  }

  public ExternalIdentityTag getExternalIdentityTag() {
    return getTypeSpecificTags(ExternalIdentityTag.class).getFirst();
  }

  private static String defaultContentFromFormulaOperators(IdentifierTag identifierTag, List<FormulaEvent> formulaEvents) {
    List<IdentifierTag> identifierTags = formulaEvents.stream()
        .map(FormulaEvent::getBadgeDefinitionAwardEvent)
        .map(BadgeDefinitionAwardEvent::getIdentifierTag).toList();

    Optional.of(
            identifierTags.stream().distinct().count() != formulaEvents.size())
        .filter(Boolean::booleanValue)
        .map(bool -> {
          throw new NostrException(String.format(CONCAT, identifierTags));
        });

    return String.format("%s == (previous)%s%s", identifierTag.getUuid(), identifierTag.getUuid(), operatorFormatDisplayIterator(formulaEvents));
  }

  private static String operatorFormatDisplayIterator(List<FormulaEvent> formulaEvents) {
    StringBuilder sb = new StringBuilder();
    formulaEvents.forEach(formula -> sb
        .append(" ")
        .append(formula.getFormula())
        .append("(")
        .append(formula.getBadgeDefinitionAwardEvent().getIdentifierTag().getUuid())
        .append(")"));
    return sb.toString();
  }

  @Override
  public List<AddressTag> getContainedEventsAsAddressTags() {
    return formulaEvents.stream()
        .map(FormulaEvent::getContainedEventsAsAddressTags)
        .flatMap(List::stream).toList();
  }
}
