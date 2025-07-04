package com.prosilion.nostr.event.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.prosilion.nostr.tag.Key;
import java.net.URI;
import java.util.Optional;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.util.HtmlUtils;

@JsonPropertyOrder({"uri", "relayInformationDocument"})
public record Relay(
    @Getter @Key URI uri,
    @Getter @Key @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) @EqualsExclude RelayInformationDocument relayInformationDocument) {
  public Relay(@NonNull URI uri) {
    this(uri, null);
  }

  public Relay(@NonNull String uri) {
    this(URI.create(uri), null);
  }

  public Relay(@NonNull URI uri, RelayInformationDocument relayInformationDocument) {
    this.uri = uri;
    this.relayInformationDocument = relayInformationDocument;
  }

  public Relay(@NonNull String uri, RelayInformationDocument relayInformationDocument) {
    this(URI.create(uri), relayInformationDocument);
  }

  public String getScheme() {
    return uri.getScheme();
  }

  public String getHost() {
    return uri.getHost();
  }

  public Optional<String> getName() {
    return Optional.ofNullable(relayInformationDocument).map(RelayInformationDocument::getName);
  }

  public @NonNull String toString() {
    return HtmlUtils.htmlEscape(uri.toASCIIString());
  }
}
