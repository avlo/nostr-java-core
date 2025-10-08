package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.crypto.HexStringValidator;
import com.prosilion.nostr.enums.Marker;
import com.prosilion.nostr.event.internal.Relay;
import java.util.Optional;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Tag(code = "e", name = "event")
@JsonPropertyOrder({"idEvent", "recommendedRelayUrl", "marker"})
public record EventTag(
    @Getter @Key String idEvent,
    @Getter @Key @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String recommendedRelayUrl,
    @Getter @Key @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) Marker marker) implements BaseTag {

  public EventTag(@NonNull String idEvent) {
    this(idEvent, null);
  }

  public EventTag(@NonNull String idEvent, String recommendedRelayUrl) {
    this(idEvent, recommendedRelayUrl, null);
  }

  public EventTag(@NonNull String idEvent, String recommendedRelayUrl, Marker marker) {
    this.idEvent = HexStringValidator.validateHex(idEvent, 64);
    this.marker = marker;
    this.recommendedRelayUrl = urlValidator(recommendedRelayUrl);
  }

  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new EventTag(
        Optional.of(node.get(1)).orElseThrow().asText(),
        urlValidator(Optional.ofNullable(node.get(2)).map(JsonNode::asText).orElse(null)),
        Optional.ofNullable(node.get(3)).map(n -> Marker.valueOf(n.asText().toUpperCase())).orElse(null));
  }

  private static String urlValidator(String url) {
    return Strings.isBlank(url) ? url : new Relay(url).getUrl();
  }
}
