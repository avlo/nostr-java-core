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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import org.apache.logging.log4j.util.Strings;

@Getter
public class BadgeDefinitionReputationEvent extends BadgeDefinitionGenericEvent implements TagMappedEventIF {
  public static final String MISSING_FORMULA_EVENTS = "BadgeDefinitionReputationEvent ctor() is missing FormulaEvent(s) parameter";
  public static final String MATCHING_IDENTIFIER_TAGS_FOUND = "Formula events containing illegal matching identifier tags found: ";
  public static final String CONCAT_INVALID_MATCHING_TAGS = Strings.concat(MATCHING_IDENTIFIER_TAGS_FOUND, " %s");

  @JsonIgnore
  private final List<FormulaEvent> formulaEvents; // aTags

  public BadgeDefinitionReputationEvent(
     @NonNull Identity aImgIdentity,
     @NonNull PublicKey reputationDefinitionCreatorPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull Relay relay,
     @NonNull ExternalIdentityTag externalIdentityTag,
     @NonNull FormulaEvent... formulaEvent) throws NostrException {
    this(
       aImgIdentity,
       reputationDefinitionCreatorPublicKey,
       identifierTag,
       relay,
       externalIdentityTag,
       List.of(formulaEvent));
  }

  public BadgeDefinitionReputationEvent(
     @NonNull Identity aImgIdentity,
     @NonNull PublicKey reputationDefinitionCreatorPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull Relay relay,
     @NonNull ExternalIdentityTag externalIdentityTag,
     @NonNull List<FormulaEvent> formulaEvents) throws NostrException {
    this(aImgIdentity, reputationDefinitionCreatorPublicKey, identifierTag, relay, externalIdentityTag, formulaEvents, List.of());
  }

  public BadgeDefinitionReputationEvent(
     @NonNull Identity aImgIdentity,
     @NonNull PublicKey reputationDefinitionCreatorPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull Relay relay,
     @NonNull ExternalIdentityTag externalIdentityTag,
     @NonNull List<FormulaEvent> formulaEvents,
     @NonNull List<BaseTag> baseTags) throws NostrException {
    super(
       aImgIdentity,
       identifierTag,
       Stream.concat(
          Stream.concat(
             TagMappedEventIF.throwIfEmpty(formulaEvents, MISSING_FORMULA_EVENTS)
                .map(AddressableEvent::asAddressableEventAddressTag),
             Stream.of(new PubKeyTag(reputationDefinitionCreatorPublicKey))),
          Stream.concat(
             Stream.of(externalIdentityTag),
             baseTags.stream()
                .filter(Predicate.not(IdentifierTag.class::isInstance))
                .filter(Predicate.not(AddressTag.class::isInstance))
                .filter(Predicate.not(PubKeyTag.class::isInstance)))),
       defaultContentFromFormulaOperators(identifierTag, formulaEvents), relay);
    this.formulaEvents = formulaEvents;
  }

  public BadgeDefinitionReputationEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull Function<AddressTag, FormulaEvent> eventTagFormulaEventFunction) {
    super(genericEventRecord);
    this.formulaEvents = mapTagsToEvents(this, eventTagFormulaEventFunction, AddressTag.class);
  }

  @JsonIgnore
  public final ExternalIdentityTag getExternalIdentityTag() {
    return requireFirstTag(ExternalIdentityTag.class);
  }

  @JsonIgnore
  public final PublicKey getReputationDefinitionCreatorPublicKey() {
    return requireFirstTag(PubKeyTag.class).publicKey();
  }

  private static String defaultContentFromFormulaOperators(IdentifierTag identifierTag, List<FormulaEvent> formulaEvents) {
    final Set<FormulaEvent> distinctFormulaEvents = new HashSet<>(formulaEvents);
    NostrException.testBoolean(
       Objects.equals(
          Long.valueOf(
             distinctFormulaEvents.stream()
                .map(FormulaEvent::getBadgeDefinitionGenericEvent)
                .map(BadgeDefinitionGenericEvent::getIdentifierTag)
                .distinct().count()).intValue(),
          formulaEvents.size()),
       String.format(CONCAT_INVALID_MATCHING_TAGS, distinctFormulaEvents.stream()
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
       "BadgeDefinitionReputationEvent FormulaEvent(s) operator(s) default content",
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
