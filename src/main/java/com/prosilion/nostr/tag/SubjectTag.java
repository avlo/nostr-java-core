package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "subject")
public record SubjectTag(
    @Getter @Key String subject) implements BaseTag {

  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new SubjectTag(Optional.of(node.get(1)).orElseThrow().asText());
  }
}
