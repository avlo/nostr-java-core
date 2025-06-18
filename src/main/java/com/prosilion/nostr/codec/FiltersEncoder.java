package com.prosilion.nostr.codec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prosilion.nostr.filter.Filters;

public class FiltersEncoder implements Encoder<Filters> {

  @Override
  public String encode(Filters filters) {
    ObjectNode root = createObjectNode();

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
