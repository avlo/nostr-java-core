package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.GenericEventKindIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.GeohashTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GeohashTagFilter extends AbstractFilterable<GeohashTag> {
  public final static String FILTER_KEY = "#g";

  public GeohashTagFilter(GeohashTag geohashTag) {
    super(geohashTag, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventKindIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTags(GeohashTag.class, genericEvent).stream().anyMatch(geoHashTag ->
            geoHashTag.getLocation().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getGeoHashTag().getLocation();
  }

  private GeohashTag getGeoHashTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new GeohashTagFilter(new GeohashTag(node.asText()));
}
