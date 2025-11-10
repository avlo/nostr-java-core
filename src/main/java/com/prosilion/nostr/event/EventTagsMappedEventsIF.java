package com.prosilion.nostr.event;

import com.prosilion.nostr.tag.EventTag;
import java.util.List;
import java.util.function.Function;

public interface EventTagsMappedEventsIF {

  default <T extends BaseEvent> List<T> mapEventTagEvents(BaseEvent baseEvent, Function<EventTag, T> eventTagFormulaEventFunction) {
    return baseEvent.getTypeSpecificTags(EventTag.class)
        .stream()
        .map(eventTagFormulaEventFunction).toList();
  }
}
