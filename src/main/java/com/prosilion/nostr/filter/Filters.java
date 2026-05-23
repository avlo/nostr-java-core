package com.prosilion.nostr.filter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import static java.util.stream.Collectors.groupingBy;

public record Filters(
    @NonNull @Getter Map<String, List<Filterable>> filtersMap,
    @Getter Integer limit) {

  public static final int DEFAULT_FILTERS_LIMIT = 10;
  public static final String FILTERS_CANNOT_BE_EMPTY = "Filters cannot be empty.";
  public static final String FILTER_NOT_DEFINED = "Filter key for filterable [%s] is not defined";

  public Filters(@NonNull List<Filterable> filterablesByDefaultType) {
    this(filterablesByDefaultType.stream().collect(groupingBy(Filterable::getFilterKey)), DEFAULT_FILTERS_LIMIT);
  }

  public Filters(@NonNull Filterable... filterablesByDefaultType) {
    this(List.of(filterablesByDefaultType));
  }

  public Filters(@NonNull List<Filterable> filterablesByDefaultType, Integer limit) {
    this(filterablesByDefaultType.stream().collect(groupingBy(Filterable::getFilterKey)), limit);
  }

  public Filters(@NonNull Map<String, List<Filterable>> filtersMap, Integer limit) {
    this.filtersMap = validateFiltersMap(filtersMap);
    this.limit = limit;
  }

  public void add(@NonNull Filterable... filterablesByDefaultType) {
    filtersMap.putAll(
        validateFiltersMap(
            Stream.of(filterablesByDefaultType)
                .collect(
                    groupingBy(Filterable::getFilterKey))));
  }

  public List<Filterable> getFilterByType(@NonNull String type) {
    return Objects.nonNull(filtersMap.get(type)) ? filtersMap.get(type) : List.of();
  }

  private static Map<String, List<Filterable>> validateFiltersMap(Map<String, List<Filterable>> filtersMap) throws IllegalArgumentException {
    if (filtersMap.isEmpty()) {
      throw new IllegalArgumentException(FILTERS_CANNOT_BE_EMPTY);
    }

    filtersMap.values().forEach(filterables -> {
      if (filterables.isEmpty()) {
        throw new IllegalArgumentException(FILTERS_CANNOT_BE_EMPTY);
      }
    });

    filtersMap.forEach((key, value) -> {
      if (key.isEmpty())
        throw new IllegalArgumentException(String.format(FILTER_NOT_DEFINED, value.getFirst().getFilterKey()));
    });

    return filtersMap;
  }

  @Override
  public @NonNull String toString() {
    return toStringWithPadding(0);
  }

  public @NonNull String toString(int padding) {
    return toStringWithPadding(
        Math.max(filtersMap.keySet().stream().map(String::length).max(Comparator.naturalOrder()).orElse(padding), padding));
  }

  private String toStringWithPadding(int maxPadding) {
    return filtersMap.entrySet().stream()
        .flatMap(entry ->
            entry.getValue().stream()
                .map(value ->
                    String.format("%s: %s", StringUtils.leftPad(entry.getKey(), maxPadding), value.getFilterable().toString())))
        .collect(Collectors.joining(",\n"));
  }
}
