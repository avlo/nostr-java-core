package com.prosilion.nostr.filter.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.GenericEventKindIF;
import com.prosilion.nostr.event.GenericEventId;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class EventFilter<T extends GenericEventId> extends AbstractFilterable<T> {
  public final static String FILTER_KEY = "ids";

  public EventFilter(T event) {
    super(event, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventKindIF> getPredicate() {
    return (genericEvent) ->
        genericEvent.getId().equals(getFilterableValue());
  }

  @Override
  public String getFilterableValue() {
    return getEvent().getId();
  }

  private GenericEventId getEvent() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new EventFilter<>(new GenericEventId(node.asText()));
}
