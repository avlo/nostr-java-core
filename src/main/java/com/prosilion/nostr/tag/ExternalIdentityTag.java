package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.ExternalIdentityTagSerializer;
import com.prosilion.nostr.enums.Kind;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "i")
@JsonPropertyOrder({"kind", "identifierTag", "formula"})
@JsonSerialize(using = ExternalIdentityTagSerializer.class)
public record ExternalIdentityTag(
    @Getter @Key Kind kind,
    @Getter @Key IdentifierTag identifierTag,
    @Getter @Key String formula) implements BaseTag {

  public static BaseTag deserialize(@NonNull JsonNode node) {
    List<String> list = Arrays.stream(node.get(1).asText().split(":")).toList();

    return new ExternalIdentityTag(
        Kind.valueOf(Integer.parseInt(Optional.ofNullable(list.get(0)).orElseThrow())),
        Optional.ofNullable(list.get(1)).map(IdentifierTag::new).orElseThrow(),
        Optional.ofNullable(list.get(2)).orElseThrow()
    );
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ExternalIdentityTag that = (ExternalIdentityTag) o;
    return Objects.equals(kind, that.kind) &&
        Objects.equals(identifierTag, that.identifierTag) &&
        Objects.equals(formula, that.formula);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kind, identifierTag, formula);
  }
}
