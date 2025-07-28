package com.prosilion.nostr.filter.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventId;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class EventFilter extends AbstractFilterable<GenericEventId> {
  public final static String FILTER_KEY = "ids";

  public EventFilter(GenericEventId event) {
    super(event, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        genericEvent.getEventId().equals(getFilterableValue());
  }

  @Override
  public String getFilterableValue() {
    return getEvent().getId();
  }

  private GenericEventId getEvent() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new EventFilter(new GenericEventId(node.asText()));
}
