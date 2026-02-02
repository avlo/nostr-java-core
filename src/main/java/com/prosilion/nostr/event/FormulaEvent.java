package com.prosilion.nostr.event;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

@Getter
public class FormulaEvent extends ArbitraryCustomAppDataEvent implements TagMappedEventIF {
  @JsonIgnore
  private final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;

  public FormulaEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
      @NonNull String formula) throws NostrException, ParseException {
    this(
        identity,
        identifierTag,
        relay,
        badgeDefinitionGenericEvent,
        List.of(),
        formula);
  }

  public FormulaEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
      @NonNull List<BaseTag> baseTags,
      @NonNull String formula) throws NostrException, ParseException {
    super(
        identity,
        identifierTag,
        relay,
        Stream.concat(
            Stream.of(
                badgeDefinitionGenericEvent.asAddressTag()),
            baseTags.stream()
                .filter(Predicate.not(AddressTag.class::isInstance))),
        validate(formula));
    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
  }

  public FormulaEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionGenericEvent> fxn) {
    super(genericEventRecord);
    this.badgeDefinitionGenericEvent = mapTagsToEvents(this, fxn, AddressTag.class).getFirst();
  }

  @JsonIgnore
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

  @Override
  @JsonIgnore
  public List<AddressTag> getContainedAddressableEvents() {
    return List.of(badgeDefinitionGenericEvent.asAddressTag());
  }
}
