package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.ReferenceTagSerializer;
import java.net.URI;
import java.util.Optional;
import org.springframework.lang.NonNull;

@Tag(code = "r")
@JsonSerialize(using = ReferenceTagSerializer.class)
public record ReferenceTag(
    @Key String uri) implements BaseTag {
  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new ReferenceTag(Optional.of(node.get(1)).orElseThrow().asText());
  }

  public URI getUri() {
    return URI.create(uri);
  }
}
