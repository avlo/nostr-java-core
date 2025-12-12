package com.prosilion.nostr.tag;

import com.prosilion.nostr.event.internal.Relay;

public interface ReferencedAbstractEventTag extends BaseTag {
  Relay getRelay();
}
