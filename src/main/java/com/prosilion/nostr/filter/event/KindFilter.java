package com.prosilion.nostr.filter.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;

@EqualsAndHashCode(callSuper = true)
public class KindFilter extends AbstractFilterable<Kind> {
  public static final String FILTER_KEY = "kinds";

  public KindFilter(Kind kind) {
    super(kind, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        getFilterableValue().equals(genericEvent.getKind().getValue());
  }

  @Override
  public void addToArrayNode(ArrayNode arrayNode) {
    arrayNode.addAll(
        MAPPER_AFTERBURNER.createArrayNode().add(
            getFilterableValue()));
  }

  @Override
  public Integer getFilterableValue() {
    return getKind().getValue();
  }

  private Kind getKind() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new KindFilter(Kind.valueOf(node.asInt()));
}
