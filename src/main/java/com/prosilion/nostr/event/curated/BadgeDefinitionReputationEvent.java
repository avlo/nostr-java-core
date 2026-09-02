package com.prosilion.nostr.event.curated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.AddressableEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.TagMappedEventIF;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
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
  public static final String MISSING_FORMULA_EVENTS = "BadgeDefinitionReputationEvent Ctor() is missing CuratedFormulaEvent(s) parameter";
  public static final String MATCHING_IDENTIFIER_TAGS_FOUND = "Formula events containing illegal matching identifier tags found: ";
  public static final String CONCAT_INVALID_MATCHING_TAGS = Strings.concat(MATCHING_IDENTIFIER_TAGS_FOUND, " %s");

  @JsonIgnore
  private final List<CuratedFormulaEvent> curatedFormulaEvents; // aTags

  public BadgeDefinitionReputationEvent(
     @NonNull Identity reputationDefinitionCreatorIdentity,
     @NonNull PublicKey reputationHostPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull ExternalIdentityTag externalIdentityTag,
     @NonNull Relay relay,
     @NonNull CuratedFormulaEvent... formulaEvent) throws NostrException {
    this(reputationDefinitionCreatorIdentity, reputationHostPublicKey, identifierTag, externalIdentityTag, relay, List.of(formulaEvent));
  }

  public BadgeDefinitionReputationEvent(
     @NonNull Identity reputationDefinitionCreatorIdentity,
     @NonNull PublicKey reputationHostPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull ExternalIdentityTag externalIdentityTag,
     @NonNull Relay relay,
     @NonNull List<CuratedFormulaEvent> curatedFormulaEvents) throws NostrException {
    this(reputationDefinitionCreatorIdentity, reputationHostPublicKey, identifierTag, externalIdentityTag, List.of(), relay, curatedFormulaEvents);
  }

  public BadgeDefinitionReputationEvent(
     @NonNull Identity reputationDefinitionCreatorIdentity,
     @NonNull PublicKey reputationHostPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull ExternalIdentityTag externalIdentityTag,
     @NonNull List<BaseTag> baseTags,
     @NonNull Relay relay,
     @NonNull List<CuratedFormulaEvent> curatedFormulaEvents) throws NostrException {
    super(
       reputationDefinitionCreatorIdentity,
       identifierTag,
       Stream.concat(
          Stream.concat(
             TagMappedEventIF.throwIfEmpty(curatedFormulaEvents, MISSING_FORMULA_EVENTS)
                .map(AddressableEvent::asAddressableEventAddressTag),
             Stream.of(new PubKeyTag(reputationHostPublicKey))),
          Stream.concat(
             Stream.of(externalIdentityTag),
             baseTags.stream()
                .filter(Predicate.not(IdentifierTag.class::isInstance))
                .filter(Predicate.not(AddressTag.class::isInstance))
                .filter(Predicate.not(PubKeyTag.class::isInstance)))),
       defaultContentFromFormulaOperators(identifierTag, curatedFormulaEvents), relay);
    this.curatedFormulaEvents = curatedFormulaEvents;
  }

  public BadgeDefinitionReputationEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull Function<AddressTag, CuratedFormulaEvent> eventTagFormulaEventFunction) {
    super(
       validateGenericConstructorKind(genericEventRecord, Kind.BADGE_DEFINITION_EVENT));
    this.curatedFormulaEvents = mapTagsToEvents(this, eventTagFormulaEventFunction, AddressTag.class);
  }

  @JsonIgnore
  public final ExternalIdentityTag getExternalIdentityTag() {
    return requireFirstTag(ExternalIdentityTag.class);
  }

  @JsonIgnore
  public final PublicKey getReputationDefinitionCreatorPublicKey() {
    return getPublicKey();
  }

  @JsonIgnore
  public final PublicKey getReputationHostPublicKey() {
    return requireFirstTag(PubKeyTag.class).publicKey();
  }

  private static String defaultContentFromFormulaOperators(IdentifierTag identifierTag, List<CuratedFormulaEvent> formulaEvents) {
    final Set<CuratedFormulaEvent> distinctFormulaEvents = new HashSet<>(formulaEvents);
    NostrException.testBoolean(
       Objects.equals(
          Long.valueOf(
             distinctFormulaEvents.stream()
                .map(CuratedFormulaEvent::getSetsPairedEvent)
                .map(SetsPairedEvent::getDefinitionEventIdentifierTag)
                .distinct().count()).intValue(),
          formulaEvents.size()),
       String.format(CONCAT_INVALID_MATCHING_TAGS, distinctFormulaEvents.stream()
          .map(CuratedFormulaEvent::getSetsPairedEvent)
          .map(SetsPairedEvent::getDefinitionEventIdentifierTag)
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
       "BadgeDefinitionReputationEvent CuratedFormulaEvent(s) operator(s) default content",
       identifierTag.getUuid(),
       identifierTag.getUuid(),
       operatorFormatDisplayIterator(formulaEvents));
  }

  private static String operatorFormatDisplayIterator(List<CuratedFormulaEvent> formulaEvents) {
    StringBuilder sb = new StringBuilder();
    formulaEvents.forEach(formula -> sb
       .append(" ")
       .append(formula.getFormula())
       .append("(")
       .append(formula.getSetsPairedEvent().getDefinitionEventIdentifierTag().getUuid())
       .append(")"));
    return sb.toString();
  }
}
