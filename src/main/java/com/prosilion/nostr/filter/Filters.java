package com.prosilion.nostr.filter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import org.springframework.lang.NonNull;

import static java.util.stream.Collectors.groupingBy;

public record Filters(
    @Getter Map<String, List<Filterable>> filtersMap,
    @Getter Integer limit) {

  public static final int DEFAULT_FILTERS_LIMIT = 10;

  public Filters(List<Filterable> filterablesByDefaultType) {
    this(filterablesByDefaultType.stream().collect(groupingBy(Filterable::getFilterKey)), DEFAULT_FILTERS_LIMIT);
  }

  public Filters(Filterable... filterablesByDefaultType) {
    this(List.of(filterablesByDefaultType));
  }

  public Filters(List<Filterable> filterablesByDefaultType, Integer limit) {
    this(filterablesByDefaultType.stream().collect(groupingBy(Filterable::getFilterKey)), limit);
  }

  public Filters(Map<String, List<Filterable>> filtersMap, Integer limit) {
    this.filtersMap = validateFiltersMap(filtersMap);
    this.limit = limit;
  }

  public List<Filterable> getFilterByType(@NonNull String type) {
    return Objects.nonNull(filtersMap.get(type)) ? filtersMap.get(type) : List.of();
  }

  private static Map<String, List<Filterable>> validateFiltersMap(Map<String, List<Filterable>> filtersMap) throws IllegalArgumentException {
    if (filtersMap.isEmpty()) {
      throw new IllegalArgumentException("Filters cannot be empty.");
    }

    filtersMap.values().forEach(filterables -> {
      if (filterables.isEmpty()) {
        throw new IllegalArgumentException("Filters cannot be empty.");
      }
    });

    filtersMap.forEach((key, value) -> {
      if (key.isEmpty())
        throw new IllegalArgumentException(String.format("Filter key for filterable [%s] is not defined", value.getFirst().getFilterKey()));
    });

    return filtersMap;
  }
}
