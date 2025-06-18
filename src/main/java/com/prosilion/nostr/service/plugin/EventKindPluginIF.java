package com.prosilion.nostr.service.plugin;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.BaseEvent;

public interface EventKindPluginIF<T extends BaseEvent> {
  void processIncomingEvent(T event);

  Kind getKind();
}
