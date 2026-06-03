package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.internal.Relay;
import java.util.Optional;

public interface ReferencedAbstractEventTag extends BaseTag {
  @JsonIgnore
  Optional<Relay> findRelay();

  default Relay requireRelay() {
    return findRelay().orElseThrow(() ->
        new NostrException(
            String.format("AbstractEventTag [%s] does not contain a (valid) Relay", this)));
  }
}
