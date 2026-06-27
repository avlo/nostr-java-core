package com.prosilion.nostr.tag;

import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.user.PublicKey;
import java.util.Optional;

public interface SetsEventTupleNeedsAppropriateNameIF {
  String getIdEvent();
  Optional<Relay> findRelay();
  PublicKey getPublicKey();
}
