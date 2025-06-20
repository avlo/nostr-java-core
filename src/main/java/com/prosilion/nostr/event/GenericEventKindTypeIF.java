package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Type;

public interface GenericEventKindTypeIF extends GenericEventKindIF {
  GenericEventKindIF getGenericEventKind();
  Type getType();
}
