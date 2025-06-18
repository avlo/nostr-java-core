package com.prosilion.nostr.service.plugin;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.AbstractBadgeAwardEventIF;
import com.prosilion.nostr.enums.Type;

public interface EventTypePluginIF<S extends Type, T extends AbstractBadgeAwardEventIF<S>, U extends Kind> {
  void processIncomingEvent(T event);

  U getKind();
}
