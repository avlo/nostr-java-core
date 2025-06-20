package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.GenericEventKindIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.HashtagTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class HashtagTagFilter extends AbstractFilterable<HashtagTag> {
  public final static String FILTER_KEY = "#t";

  public HashtagTagFilter(HashtagTag hashtagTag) {
    super(hashtagTag, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventKindIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTags(HashtagTag.class, genericEvent).stream().anyMatch(hashtagTag ->
            hashtagTag.getHashTag().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getHashtagTag().getHashTag();
  }

  private HashtagTag getHashtagTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new HashtagTagFilter(new HashtagTag(node.asText()));
}
