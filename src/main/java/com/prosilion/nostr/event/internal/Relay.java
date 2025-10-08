package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.tag.Key;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public record Relay(@Key String url) {
  public URL getUrl() throws MalformedURLException {
    return URI.create(url).toURL();
  }
}
