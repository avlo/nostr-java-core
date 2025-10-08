package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.crypto.HexStringValidator;
import com.prosilion.nostr.enums.Marker;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.Getter;
import org.apache.commons.lang3.stream.Streams;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.util.InvalidUrlException;

@Tag(code = "e", name = "event")
@JsonPropertyOrder({"idEvent", "recommendedRelayUrl", "marker"})
public record EventTag(
    @Getter @Key String idEvent,
    @Getter @Key @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String recommendedRelayUrl,
    @Getter @Key @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) Marker marker) implements BaseTag {

  private static final Pattern URL_PATTERN_W_PORT_PASSTHROUGH
      = Pattern.compile("^((https?|wss?)://)?(((\\w+\\-*\\w+\\.)*(\\w+\\-*\\w+)(\\.[\\w]{2,})+)|(((1|2){0,1}[0-9]{1,2}\\.){3}(1|2){0,1}[0-9]{1,2}))(:[0-9]{2,5})?(/(#/)?[\\w0-9+\\/\\-]*(\\?([\\w0-9 ]+=([\\w0-9]|%[0-7][0-9a-f])+&?)*)?(#[\\w0-9]*)?)*$");

  private static final Pattern LOCALHOST
      = Pattern.compile("^((https?|wss?)://localhost)?(:[0-9]{2,5})?(/(#/)?[\\w0-9+\\/\\-]*(\\?([\\w0-9 ]+=([\\w0-9]|%[0-7][0-9a-f])+&?)*)?(#[\\w0-9]*)?)*$");

  public EventTag(@NonNull String idEvent) {
    this(idEvent, null);
  }

  public EventTag(@NonNull String idEvent, String recommendedRelayUrl) {
    this(idEvent, recommendedRelayUrl, null);
  }

  public EventTag(@NonNull String idEvent, String recommendedRelayUrl, Marker marker) {
    this.idEvent = HexStringValidator.validateHex(idEvent, 64);
    this.marker = marker;

    if (Objects.isNull(recommendedRelayUrl) || Strings.isBlank(recommendedRelayUrl)) {  // allow empty/null relayUrls...
      this.recommendedRelayUrl = recommendedRelayUrl;
      return;
    }

    if (LOCALHOST.matcher(recommendedRelayUrl).matches()) { // allow localhost variants
      this.recommendedRelayUrl = recommendedRelayUrl;
      return;
    }

    this.recommendedRelayUrl = Optional.of(recommendedRelayUrl)  // ...but validate everything else
        .filter(s -> URL_PATTERN_W_PORT_PASSTHROUGH.matcher(s).matches())
        .orElseThrow(() -> new InvalidUrlException(recommendedRelayUrl));
  }

  public static BaseTag deserialize(@NonNull JsonNode node) throws MalformedURLException {
    String text = Optional.of(node.get(1)).orElseThrow().asText();
    String recommendedRelayUrl1 = Streams.failableStream(node.get(2)).map(n -> new URI(n.asText()).toURL().toString()).stream().findFirst().orElseThrow();
    Marker marker1 = Optional.ofNullable(node.get(3)).map(n -> Marker.valueOf(n.asText().toUpperCase())).orElse(null);
    return new EventTag(
        text,
        recommendedRelayUrl1,
        marker1);
  }
}
