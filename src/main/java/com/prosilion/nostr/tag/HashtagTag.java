package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "t", nip = 12)
public record HashtagTag(
    @Getter @Key @JsonProperty("t") String hashTag) implements BaseTag {

  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new HashtagTag(
        Optional.of(node.get(1)).orElseThrow().asText());
  }
}
