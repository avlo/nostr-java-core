package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.GenericEventKindIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.IdentifierTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class IdentifierTagFilter extends AbstractFilterable<IdentifierTag> {
  public final static String FILTER_KEY = "#d";

  public IdentifierTagFilter(IdentifierTag identifierTag) {
    super(identifierTag, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventKindIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTags(IdentifierTag.class, genericEvent).stream()
            .anyMatch(genericEventIdentifierTag ->
                genericEventIdentifierTag.getUuid().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getIdentifierTag().getUuid();
  }

  private IdentifierTag getIdentifierTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new IdentifierTagFilter(new IdentifierTag(node.asText()));
}
