package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.GenericEventKindIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.ReferenceTag;
import java.net.URI;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ReferenceTagFilter extends AbstractFilterable<ReferenceTag> {
  public final static String FILTER_KEY = "#r";

  public ReferenceTagFilter(ReferenceTag referencedPubKeyTag) {
    super(referencedPubKeyTag, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventKindIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTags(ReferenceTag.class, genericEvent).stream()
            .anyMatch(referenceTag ->
                referenceTag.getUri().toString().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getReferenceTag().getUri().toString();
  }

  private ReferenceTag getReferenceTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new ReferenceTagFilter(new ReferenceTag(URI.create(node.asText())));
}
