package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.internal.ElementAttribute;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.filter.GenericTagQuery;
import com.prosilion.nostr.tag.GenericTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GenericTagQueryFilter extends AbstractFilterable<GenericTagQuery> {
  public static final String HASH_PREFIX = "#";

  public GenericTagQueryFilter(GenericTagQuery genericTagQuery) {
    super(genericTagQuery, genericTagQuery.getTagName());
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTags(GenericTag.class, genericEvent).stream()
            .filter(genericTag ->
                genericTag.getCode().equals(stripLeadingHashTag()))
            .anyMatch(genericTag ->
                genericTag
                    .getAttributes().stream().map(
                        ElementAttribute::getValue).toList()
                    .contains(
                        getFilterableValue()));
  }

  @Override
  public String getFilterKey() {
    return getGenericTagQuery().getTagName();
  }

  @Override
  public String getFilterableValue() {
    return getGenericTagQuery().getValue();
  }

  private GenericTagQuery getGenericTagQuery() {
    return super.getFilterable();
  }

  private String stripLeadingHashTag() {
    return getFilterKey().startsWith(HASH_PREFIX) ?
        getFilterKey().substring(1) :
        getFilterKey();
  }

  public static Function<JsonNode, Filterable> fxn(String type) {
    return node -> new GenericTagQueryFilter(new GenericTagQuery(type, node.asText()));
  }
}
