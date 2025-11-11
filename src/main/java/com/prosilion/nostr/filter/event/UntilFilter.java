package com.prosilion.nostr.filter.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;

@EqualsAndHashCode(callSuper = true)
public class UntilFilter extends AbstractFilterable<Long> {
  public static final String FILTER_KEY = "until";

  public UntilFilter(Long until) {
    super(until, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        genericEvent.getCreatedAt() < getUntil();
  }

  @Override
  public ObjectNode toObjectNode(ObjectNode objectNode) {
    return MAPPER_AFTERBURNER.createObjectNode().put(FILTER_KEY, getUntil());
  }

  @Override
  public String getFilterableValue() {
    return getUntil().toString();
  }

  private Long getUntil() {
    return super.getFilterable();
  }

  public static Function<JsonNode, List<Filterable>> fxn = node -> List.of(new UntilFilter(node.asLong()));
}
