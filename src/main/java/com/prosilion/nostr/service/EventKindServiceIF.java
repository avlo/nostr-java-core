package com.prosilion.nostr.service;

import com.prosilion.nostr.event.BaseEvent;

public interface EventKindServiceIF<T extends BaseEvent> {
  void processIncomingEvent(T event);
}
