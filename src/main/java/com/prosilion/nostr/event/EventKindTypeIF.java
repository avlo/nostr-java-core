package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.Type;

public interface EventKindTypeIF<T extends Type> {
  Kind getKind();
  T getType();
}
