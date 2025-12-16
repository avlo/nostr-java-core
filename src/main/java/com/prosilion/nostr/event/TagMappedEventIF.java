package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.ReferencedAbstractEventTag;
import java.util.List;
import java.util.function.Function;

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

  List<? extends ReferencedAbstractEventTag> getContainedAddressableEvents();

  String getId();
  Kind getKind();
}
