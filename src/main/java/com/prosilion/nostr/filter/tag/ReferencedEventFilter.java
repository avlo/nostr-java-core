package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.EventTag;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;

@EqualsAndHashCode(callSuper = true)
public class ReferencedEventFilter extends AbstractFilterable<EventTag> {
  public static final String FILTER_KEY = "#e";

  public ReferencedEventFilter(EventTag referencedEventTag) {
//    super(validate(referencedEventTag), FILTER_KEY); // unused for now, see bottom
    super(referencedEventTag, FILTER_KEY);
  }

  @Override
  public final Predicate<EventIF> getPredicate() {
    return getPredicate(EventTag.class, getReferencedEventTag());
  }

  @Override
  public final String getFilterableValue() {
    return String.join("\",\"",
       getReferencedEventTag().getIdEvent(),
       getReferencedEventTag().getRecommendedRelayUrl());
  }

  private EventTag getReferencedEventTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node ->
     new ReferencedEventFilter(
        createEventTag(node));

  public static EventTag createEventTag(@NonNull JsonNode node) {
    List<String> attributes = Arrays.stream(node.get(0).asText().split("\",\"")).toList();
    return new EventTag(
       attributes.get(0),
       attributes.get(1));
  }

  @Override
  public final void addToArrayNode(ArrayNode arrayNode) {
    arrayNode.add(
       MAPPER_AFTERBURNER.createArrayNode()
          .add(getFilterableValue())
    );
  }

  //  unused for now  
  private static EventTag validate(EventTag eventTag) {
    eventTag.requireRelay();
    return eventTag;
  }
}
