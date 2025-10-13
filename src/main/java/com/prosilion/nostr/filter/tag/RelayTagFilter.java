package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.RelayTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

@EqualsAndHashCode(callSuper = true)
public class RelayTagFilter extends AbstractFilterable<RelayTag> {
  public final static String FILTER_KEY = "relay";

  public RelayTagFilter(@NonNull RelayTag relayTag) {
    super(relayTag, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTagsStream(RelayTag.class, genericEvent)
            .anyMatch(relayTag ->
                relayTag.getRelay().equals(getFilterableValue()));
  }

  @Override
  public Relay getFilterableValue() {
    return getReferenceTag().getRelay();
  }

  private RelayTag getReferenceTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new RelayTagFilter(new RelayTag(new Relay(node.asText())));
}
