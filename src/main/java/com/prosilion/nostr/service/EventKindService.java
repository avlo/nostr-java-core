package com.prosilion.nostr.service;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.TextNoteEvent;
import com.prosilion.nostr.service.plugin.EventKindPluginIF;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class EventKindService<T extends TextNoteEvent> implements EventKindServiceIF<T> {
  private final Map<Kind, EventKindPluginIF<T>> eventTypePluginsMap;


  public EventKindService(List<EventKindPluginIF<T>> eventTypePlugins) {
    this.eventTypePluginsMap = eventTypePlugins.stream().collect(
        Collectors.toMap(EventKindPluginIF::getKind, Function.identity()));
  }

  @Override
  public void processIncomingEvent(@NonNull T event) {
    Optional.ofNullable(
            eventTypePluginsMap.get(
                event.getKind()))
        .orElse(
            eventTypePluginsMap.get(Kind.TEXT_NOTE)).processIncomingEvent(event); // everything else handled as TEXT_NOTE kind
  }
}
