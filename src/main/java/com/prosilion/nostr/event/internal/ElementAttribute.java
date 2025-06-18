package com.prosilion.nostr.event.internal;

import java.util.Objects;
import lombok.Getter;

public record ElementAttribute(
    @Getter String name,
    @Getter Object value) {

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ElementAttribute that = (ElementAttribute) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
