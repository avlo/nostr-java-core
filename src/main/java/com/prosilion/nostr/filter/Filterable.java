package com.prosilion.nostr.filter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.tag.BaseTag;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.NonNull;

import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;

public interface Filterable {
  Predicate<EventIF> getPredicate();
  <T> T getFilterable();
  Object getFilterableValue();
  String getFilterKey();

  default Predicate<EventIF> getPredicate(@NonNull Class<? extends BaseTag> clazz, @NonNull BaseTag baseTag) {
    return (eventIF) ->
        eventIF.findFirstTag(clazz)
            .map(baseTag::equals)
            .orElse(false);
  }

  default ObjectNode toObjectNode(ObjectNode objectNode) {
    ArrayNode arrayNode = MAPPER_AFTERBURNER.createArrayNode();

    Optional.ofNullable(objectNode.get(getFilterKey()))
        .ifPresent(jsonNode ->
            jsonNode.elements().forEachRemaining(arrayNode::add));

    addToArrayNode(arrayNode);

    return objectNode.set(getFilterKey(), arrayNode);
  }

  default void addToArrayNode(ArrayNode arrayNode) {
    arrayNode.addAll(
        MAPPER_AFTERBURNER.createArrayNode().add(
            getFilterableValue().toString()));
  }
}
