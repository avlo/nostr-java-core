package com.prosilion.nostr.event;

import com.prosilion.nostr.user.ISignableEntity;

public interface GenericEventKindIF extends EventIF, IEvent, ISignableEntity {
  String toBech32();
  String toString();
  boolean equals(Object o);
  int hashCode();
}
