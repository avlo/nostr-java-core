package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.tag.Key;
import java.net.URI;

public record Relay(@Key String uri) {
  public URI getUri() {
    return URI.create(uri);
  }
}
