package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.tag.Key;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.Getter;
import org.springframework.lang.NonNull;

public record Relay(@Getter @Key URI uri) {
  public Relay(@NonNull String uri) throws URISyntaxException {
    this(new URI(uri));
  }
}
