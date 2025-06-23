package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.KindTypeIF;
import java.util.List;

public interface GenericEventKindTypeIF extends GenericEventKindIF {
  GenericEventKindIF genericEventKind();
  List<KindTypeIF> getDefinedKindTypes();
}
