package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Type;

public interface GenericEventKindTypeIF<T extends Type> extends GenericEventKindIF {
  GenericEventKindIF getGenericEventKind();
  T getType();
}
