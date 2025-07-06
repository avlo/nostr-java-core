package com.prosilion.nostr.codec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prosilion.nostr.filter.Filters;

public class FiltersEncoder {
  public static String encode(Filters filters) {
    ObjectNode root = Encoder.createObjectNode();

    filters.getFiltersMap().forEach((key, filterableList) ->
        root.setAll(
            filterableList
                .stream()
                .map(filterable ->
                    filterable.toObjectNode(root))
                .toList()
                .getFirst()));

    return root.toString();
  }
}
