package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.KindType;

public interface GenericEventKindTypeIF extends GenericEventKindIF {
  GenericEventKindIF getGenericEventKind();
  KindType getKindType();
}
