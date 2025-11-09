package com.prosilion.nostr.filter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@EqualsAndHashCode
public abstract class AbstractFilterable<T> implements Filterable {
  private final T filterable;
  private final String filterKey;

  protected AbstractFilterable(@NonNull T filterable, @NonNull String filterKey) {
    this.filterable = filterable;
    this.filterKey = filterKey;
  }
}
