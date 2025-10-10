package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.RelaysTagSerializer;
import com.prosilion.nostr.event.internal.Relay;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "relays")
@JsonSerialize(using = RelaysTagSerializer.class)
public record RelaysTag(@Getter List<Relay> relays) implements BaseTag {

  public RelaysTag(@NonNull Relay... relay) {
    this(List.of(relay));
  }

  public RelaysTag(@NonNull List<Relay> relays) {
    this.relays = relays;
  }

  public static BaseTag deserialize(JsonNode node) throws MalformedURLException {
    return new RelaysTag(Optional.ofNullable(node).map(jsonNode ->
        new Relay(jsonNode.get(1).asText())).orElseThrow(MalformedURLException::new));
  }
}

