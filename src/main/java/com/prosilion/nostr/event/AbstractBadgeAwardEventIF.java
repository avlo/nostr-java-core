package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.Type;

public interface AbstractBadgeAwardEventIF<T extends Type> {
  void doSomething();
  Kind getKind();
  T getType();
}
