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
public class SinceFilter extends AbstractFilterable<Long> {
  public final static String FILTER_KEY = "since";

  public SinceFilter(Long since) {
    super(since, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        genericEvent.getCreatedAt() > getSince();
  }

  @Override
  public ObjectNode toObjectNode(ObjectNode objectNode) {
    return MAPPER_AFTERBURNER.createObjectNode().put(FILTER_KEY, getSince());
  }

  @Override
  public String getFilterableValue() {
    return getSince().toString();
  }

  private Long getSince() {
    return super.getFilterable();
  }

  public static Function<JsonNode, List<Filterable>> fxn = node -> List.of(new SinceFilter(node.asLong()));
}
