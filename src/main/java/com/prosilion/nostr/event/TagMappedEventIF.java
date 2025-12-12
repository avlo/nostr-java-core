package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.ReferencedAbstractEventTag;
import java.util.List;
import java.util.function.Function;

public interface TagMappedEventIF extends EventIF {
  default <T extends BaseEvent, U extends ReferencedAbstractEventTag> List<T> mapTagsToEvents(
      BaseEvent baseEvent,
      Function<U, T> eventTagTFunction,
      Class<U> clazz) {
    return baseEvent.getTypeSpecificTags(clazz)
        .stream()
        .map(eventTagTFunction).toList();
  }

  List<? extends ReferencedAbstractEventTag> getContainedEventsAsTags();

  String getId();
  Kind getKind();
}
