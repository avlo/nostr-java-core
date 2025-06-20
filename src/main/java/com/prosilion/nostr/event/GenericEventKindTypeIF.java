package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.KindTypeIF;

public interface GenericEventKindTypeIF extends GenericEventKindIF {
  GenericEventKindIF getGenericEventKind();

  KindTypeIF getKindType();
}
