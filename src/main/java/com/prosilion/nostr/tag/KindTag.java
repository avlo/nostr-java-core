package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.enums.Kind;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "k")
public record KindTag(
    @Getter @Key @JsonProperty("k") Kind kind) implements BaseTag {

  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new KindTag(
        Kind.valueOf(Optional.of(node.get(1)).orElseThrow().asInt()));
  }
}
