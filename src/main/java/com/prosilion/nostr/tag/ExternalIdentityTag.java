package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.ExternalIdentityTagSerializer;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "i")
@JsonSerialize(using = ExternalIdentityTagSerializer.class)
public record ExternalIdentityTag(
    @Getter @Key String platform,
    @Getter @Key String identity,
    @Getter @Key String proof) implements BaseTag {

  public static ExternalIdentityTag deserialize(@NonNull JsonNode node) {
    List<String> platformAndIdentity = Arrays.stream(node.get(0).asText().split(":")).toList();
    return new ExternalIdentityTag(platformAndIdentity.get(0), platformAndIdentity.get(1), node.get(1).asText());
  }
}
