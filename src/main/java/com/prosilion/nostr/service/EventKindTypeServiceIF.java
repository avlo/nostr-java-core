package com.prosilion.nostr.service;

import com.prosilion.nostr.event.AbstractBadgeAwardEventIF;
import com.prosilion.nostr.enums.Type;

public interface EventKindTypeServiceIF<T extends Type, U extends AbstractBadgeAwardEventIF<T>> {
  void processIncomingEvent(U event);
}
