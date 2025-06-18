package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.GenericEventDtoIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.GeohashTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GeohashTagFilter<T extends GeohashTag> extends AbstractFilterable<T> {
  public final static String FILTER_KEY = "#g";

  public GeohashTagFilter(T geohashTag) {
    super(geohashTag, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventDtoIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTags(GeohashTag.class, genericEvent).stream().anyMatch(geoHashTag ->
            geoHashTag.getLocation().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getGeoHashTag().getLocation();
  }

  private T getGeoHashTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new GeohashTagFilter<>(new GeohashTag(node.asText()));
}
