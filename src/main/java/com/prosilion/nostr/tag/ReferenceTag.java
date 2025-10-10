package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.ReferenceTagSerializer;
import com.prosilion.nostr.event.internal.Relay;
import java.net.MalformedURLException;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "r")
@JsonSerialize(using = ReferenceTagSerializer.class)
public record ReferenceTag(@Getter @Key String url) implements BaseTag {

  public ReferenceTag(@NonNull String url) {
    this.url = new Relay(url).getUrl();
  }

  public static BaseTag deserialize(@NonNull JsonNode node) throws MalformedURLException {
    return Optional.of(node).map(jsonNode ->
        new ReferenceTag(jsonNode.get(1).asText())).orElseThrow(MalformedURLException::new);
  }
}
