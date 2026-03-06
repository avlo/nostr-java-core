package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.EventTagSerializer;
import com.prosilion.nostr.crypto.HexStringValidator;
import com.prosilion.nostr.enums.Marker;
import com.prosilion.nostr.event.internal.Relay;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Tag(code = "e", name = "event")
@JsonPropertyOrder({"idEvent", "recommendedRelayUrl", "marker"})
@JsonSerialize(using = EventTagSerializer.class)
public record EventTag(
    @Getter @Key String idEvent,
    @Getter @Key @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String recommendedRelayUrl,
    @Getter @Key @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) Marker marker) implements ReferencedAbstractEventTag {

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

  @JsonIgnore
  @Override
  public Optional<Relay> getAbstractEventRelay() {
    return Optional.ofNullable(recommendedRelayUrl).map(Relay::new);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    EventTag eventTag = (EventTag) o;
    return Objects.equals(idEvent, eventTag.idEvent);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(idEvent);
  }
}
