package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.RelayTagSerializer;
import com.prosilion.nostr.event.internal.Relay;
import java.net.MalformedURLException;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * RelayTag (this class) refers to relay bound to an event
 * whereas
 *
 * @see RelaysTag is used to refer to other relay(s)
 */
@Tag(code = "relay")
@JsonSerialize(using = RelayTagSerializer.class)
public record RelayTag(
   @Getter @Key @JsonInclude(JsonInclude.Include.NON_NULL) Relay relay) implements BaseTag {
  public static BaseTag deserialize(JsonNode node) throws MalformedURLException {
    return new RelayTag(Optional.ofNullable(node).map(jsonNode ->
       new Relay(jsonNode.get(1).asText())).orElseThrow(MalformedURLException::new));
  }

//  @JsonIgnore
//  public Optional<Relay> findRelay() {
//    return Optional.ofNullable(relay);
//  }
//
//  @JsonIgnore
//  public Relay requireRelay() {
//    return Optional.of(relay).orElseThrow(() ->
//       new NostrException("RelayTag relay is null"));
//  }
}

