package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;

@Getter
public class BadgeDefinitionReputationEvent extends BadgeDefinitionGenericEvent implements TagMappedEventIF {
  public static final String MESSAGE = "BadgeDefinitionReputationEvent ctor() is missing FormulaEvent(s) parameter";
  public static final String MATCHING_IDENTIFIER_TAGS_FOUND = "Formula events containing illegal matching identifier tags found:";
  public static final String CONCAT = Strings.concat(MATCHING_IDENTIFIER_TAGS_FOUND, " [%s]");

  @JsonIgnore
  private final List<FormulaEvent> formulaEvents; // aTags

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull PublicKey reputationDefinitionCreatorPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull FormulaEvent... formulaEvent) throws NostrException {
    this(
        identity,
        reputationDefinitionCreatorPublicKey,
        identifierTag,
        relay,
        externalIdentityTag,
        List.of(formulaEvent));
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull PublicKey reputationDefinitionCreatorPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull List<FormulaEvent> formulaEvents) throws NostrException {
    this(identity, reputationDefinitionCreatorPublicKey, identifierTag, relay, externalIdentityTag, formulaEvents, List.of());
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull PublicKey reputationDefinitionCreatorPublicKey,
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
            Stream.concat(
                TagMappedEventIF.throwIfEmpty(formulaEvents, MESSAGE)
                    .map(AddressableEvent::asAddressableEventAddressTag),
                Stream.of(new PubKeyTag(reputationDefinitionCreatorPublicKey))),
            Stream.concat(
                Stream.of(externalIdentityTag),
                baseTags.stream()
                    .filter(Predicate.not(IdentifierTag.class::isInstance))
                    .filter(Predicate.not(AddressTag.class::isInstance))
                    .filter(Predicate.not(PubKeyTag.class::isInstance)))),
        defaultContentFromFormulaOperators(identifierTag, formulaEvents));
    this.formulaEvents = formulaEvents;
  }

  public BadgeDefinitionReputationEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, FormulaEvent> eventTagFormulaEventFunction) {
    super(genericEventRecord);
    this.formulaEvents = mapTagsToEvents(this, eventTagFormulaEventFunction, AddressTag.class);
  }

  @JsonIgnore
  public ExternalIdentityTag getExternalIdentityTag() {
    return requireFirstTag(ExternalIdentityTag.class);
  }

  @JsonIgnore
  public PublicKey getReputationDefinitionCreatorPublicKey() {
    return requireFirstTag(PubKeyTag.class).publicKey();
  }

  private static String defaultContentFromFormulaOperators(IdentifierTag identifierTag, List<FormulaEvent> formulaEvents) {
    NostrException.testBoolean(
        Objects.equals(
            Long.valueOf(
                formulaEvents.stream()
                    .map(FormulaEvent::getBadgeDefinitionGenericEvent)
                    .map(BadgeDefinitionGenericEvent::getIdentifierTag)
                    .distinct().count()).intValue(),
            formulaEvents.size()),
        String.format(CONCAT, formulaEvents.stream()
            .map(FormulaEvent::getBadgeDefinitionGenericEvent)
            .map(BadgeDefinitionGenericEvent::getIdentifierTag)
            .toList()));

//  TODO (potentially): to accommodate both (necessary) formula as well as (optional) user-defined comment/text,
//   introduce "summary"/"description" tag as per:    
/*
  ["summary", "<brief description of the event>"],
  https://github.com/nostr-protocol/nips/blob/master/52.md
  
  ["description", "Awarded to users demonstrating bravery"],
  A description tag whose value contain meaning behind the badge, or the reason of its issuance.
  https://github.com/nostr-protocol/nips/blob/master/58.md    
*/
    return String.format("%s: %s == (previous)%s%s",
        "SuperConductor BadgeDefinitionReputationEvent, default content from FormulaEvent(s) operator(s)",
        identifierTag.getUuid(),
        identifierTag.getUuid(),
        operatorFormatDisplayIterator(formulaEvents));
  }

  private static String operatorFormatDisplayIterator(List<FormulaEvent> formulaEvents) {
    StringBuilder sb = new StringBuilder();
    formulaEvents.forEach(formula -> sb
        .append(" ")
        .append(formula.getFormula())
        .append("(")
        .append(formula.getBadgeDefinitionGenericEvent().getIdentifierTag().getUuid())
        .append(")"));
    return sb.toString();
  }
}
