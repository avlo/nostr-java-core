package com.prosilion.nostr.filter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prosilion.nostr.event.GenericEventDtoIF;
import com.prosilion.nostr.tag.BaseTag;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;

public interface Filterable {
  Predicate<GenericEventDtoIF> getPredicate();
  <T> T getFilterable();
  Object getFilterableValue();
  String getFilterKey();

  static <T extends BaseTag> List<T> getTypeSpecificTags(@NonNull Class<T> tagClass, @NonNull GenericEventDtoIF event) {
    return event.getTags().stream()
        .filter(tagClass::isInstance)
        .map(tagClass::cast)
        .toList();
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
