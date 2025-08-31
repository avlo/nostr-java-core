package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.IdentifierTagSerializer;
import java.time.temporal.ValueRange;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

@Tag(code = "d")
@JsonSerialize(using = IdentifierTagSerializer.class)
public record IdentifierTag(@Getter @Key String uuid) implements BaseTag {

  public static BaseTag deserialize(@NonNull JsonNode node) {
    String uuid = Optional.of(node.get(1)).orElseThrow().asText();
    
    if (!ValueRange.of(1, 64).isValidIntValue(uuid.length())) {
      throw new IllegalArgumentException(String.format("IdentifierTag length must be between 1 and 64 characters but was [%d]", uuid.length()));
    }
    return new IdentifierTag(uuid);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    IdentifierTag that = (IdentifierTag) o;
    return Objects.equals(uuid, that.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }
}
