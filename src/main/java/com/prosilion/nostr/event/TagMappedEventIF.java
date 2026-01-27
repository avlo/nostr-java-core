package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.ReferencedAbstractEventTag;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface TagMappedEventIF extends EventIF {
  default <T extends BaseEvent, U extends ReferencedAbstractEventTag>
  List<T> mapTagsToEvents(
      BaseEvent baseEvent,
      Function<U, T> tagMappingFunction,
      Class<U> clazz) {
    return baseEvent.getTypeSpecificTags(clazz)
        .stream()
        .map(tagMappingFunction).toList();
  }

  @JsonIgnore
  List<? extends ReferencedAbstractEventTag> getContainedAddressableEvents();

  String getId();
  Kind getKind();

  static <T> Stream<T> throwIfEmpty(List<T> list, String message) {
    if (list.isEmpty()) {
      throw new NostrException(message);
    }
    return list.stream();
  }
}
