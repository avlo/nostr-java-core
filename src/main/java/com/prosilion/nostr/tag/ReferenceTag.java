package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.ReferenceTagSerializer;
import java.net.URI;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "r", nip = 12)
@JsonSerialize(using = ReferenceTagSerializer.class)
public record ReferenceTag(
    @Getter @Key URI uri) implements BaseTag {
  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new ReferenceTag(
        URI.create(Optional.of(node.get(1)).orElseThrow().asText())
    );
  }
}
