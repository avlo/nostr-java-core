package com.prosilion.nostr.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.filter.FilterableProvider;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.filter.Filters;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;

public interface FiltersDecoder {

  static Filters decode(@NonNull String jsonFiltersList) throws JsonProcessingException {
    final List<Filterable> filterables = new ArrayList<>();

    MAPPER_AFTERBURNER.readTree(jsonFiltersList).properties().iterator().forEachRemaining(node ->
        filterables.addAll(
            FilterableProvider.getFilterFunction(
                node.getKey(),
                node.getValue())));

    return new Filters(filterables);
  }
}
