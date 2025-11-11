package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.ReferenceTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

@EqualsAndHashCode(callSuper = true)
public class ReferenceTagFilter extends AbstractFilterable<ReferenceTag> {
  public static final String FILTER_KEY = "#r";

  public ReferenceTagFilter(@NonNull ReferenceTag referencedPubKeyTag) {
    super(referencedPubKeyTag, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTagsStream(ReferenceTag.class, genericEvent)
            .anyMatch(referenceTag ->
                referenceTag.getUrl().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getReferenceTag().getUrl();
  }

  private ReferenceTag getReferenceTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new ReferenceTagFilter(new ReferenceTag(node.asText()));
}
