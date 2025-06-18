package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.IdentifierTagSerializer;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

@Tag(code = "d", nip = 1)
@JsonSerialize(using = IdentifierTagSerializer.class)
public record IdentifierTag(@Getter @Key String uuid) implements BaseTag {

  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new IdentifierTag(
        Optional.of(node.get(1)).orElseThrow().asText());
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
