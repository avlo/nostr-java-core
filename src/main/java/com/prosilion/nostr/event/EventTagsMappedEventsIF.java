package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.EventTag;
import java.util.List;
import java.util.function.Function;
import org.springframework.lang.NonNull;

public interface EventTagsMappedEventsIF {
  default <T extends BaseEvent> List<T> mapEventTagsToEvents(
      @NonNull BaseEvent baseEvent,
      @NonNull Function<EventTag, T> eventTagTFunction) {
    return baseEvent.getTypeSpecificTags(EventTag.class)
        .stream()
        .map(eventTagTFunction).toList();
  }

  List<EventTag> getContainedEventsAsEventTags();

  String getId();
  Kind getKind();
}
