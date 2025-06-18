package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.codec.serializer.RelaysTagSerializer;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Tag(code = "relays", nip = 57)
@JsonSerialize(using = RelaysTagSerializer.class)
public record RelaysTag(
    @Getter List<Relay> relays) implements BaseTag {

  public RelaysTag(Relay... relay) {
    this(List.of(relay));
  }

  public RelaysTag(List<Relay> relays) {
    this.relays = relays;
  }

  public static BaseTag deserialize(JsonNode node) {
    return new RelaysTag(Optional.ofNullable(node).map(jsonNode -> new Relay(jsonNode.get(1).asText())).orElseThrow());
  }
}
