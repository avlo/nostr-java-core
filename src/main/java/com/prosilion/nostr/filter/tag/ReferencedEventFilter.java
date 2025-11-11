package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.EventTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ReferencedEventFilter extends AbstractFilterable<EventTag> {
  public static final String FILTER_KEY = "#e";

  public ReferencedEventFilter(EventTag referencedEventTag) {
    super(referencedEventTag, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTagsStream(EventTag.class, genericEvent)
            .anyMatch(eventTag ->
                eventTag.getIdEvent().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getReferencedEventTag().getIdEvent();
  }

  private EventTag getReferencedEventTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node ->
      new ReferencedEventFilter(
          new EventTag(
              node.get(0).asText()));
}
