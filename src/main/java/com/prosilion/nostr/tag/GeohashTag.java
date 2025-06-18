package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "g", nip = 12)
public record GeohashTag(
    @Getter @Key @JsonProperty("g") String location) implements BaseTag {

  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new GeohashTag(
        Optional.of(node.get(1)).orElseThrow().asText());
  }
}
