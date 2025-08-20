package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.RelayTagSerializer;
import com.prosilion.nostr.event.internal.Relay;
import java.util.Optional;
import lombok.Getter;

@Tag(code = "relay")
@JsonSerialize(using = RelayTagSerializer.class)
public record RelayTag(@Getter Relay relay) implements BaseTag {

  public static BaseTag deserialize(JsonNode node) {
    return new RelayTag(Optional.ofNullable(node).map(jsonNode ->
        new Relay(jsonNode.get(1).asText())).orElseThrow());
  }
}

