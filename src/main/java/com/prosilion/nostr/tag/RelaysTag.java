package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.RelaysTagSerializer;
import com.prosilion.nostr.event.internal.Relay;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;

/**
 * RelaysTag (this class) is used to refer to other relay(s)
 * whereas
 *
 * @see RelayTag refers to relay bound to an event
 *
 */
@Tag(code = "relays")
@JsonSerialize(using = RelaysTagSerializer.class)
public record RelaysTag(@Getter Set<Relay> relays) implements BaseTag {

  public RelaysTag(@NonNull Relay... relay) {
    this(Set.of(relay));
  }

  public RelaysTag(@NonNull Set<Relay> relays) {
    this.relays = relays;
  }

  public static BaseTag deserialize(JsonNode node) throws MalformedURLException {
    return new RelaysTag(Optional.ofNullable(node).map(jsonNode ->
       new Relay(jsonNode.get(1).asText())).orElseThrow(MalformedURLException::new));
  }
}

