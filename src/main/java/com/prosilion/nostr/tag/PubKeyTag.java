package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.user.PublicKey;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Tag(code = "p")
@JsonPropertyOrder({"pubKey", "mainRelayUrl", "petName"})
public record PubKeyTag(
    @Getter @Key PublicKey publicKey,
    @Getter @Nullable @Key @JsonInclude(JsonInclude.Include.NON_NULL) String mainRelayUrl,
    @Getter @Nullable @Key @JsonInclude(JsonInclude.Include.NON_NULL) String petName) implements BaseTag {

  public PubKeyTag(PublicKey publicKey) {
    this(publicKey, null);
  }

  public PubKeyTag(PublicKey publicKey, String mainRelayUrl) {
    this(publicKey, mainRelayUrl, null);
  }

  public PubKeyTag(PublicKey publicKey, String mainRelayUrl, String petName) {
    this.publicKey = publicKey;
    this.mainRelayUrl = mainRelayUrl;
    this.petName = petName;
  }

  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new PubKeyTag(
        new PublicKey(
            Optional.ofNullable(node.get(1)).orElseThrow().asText()),
        Optional.ofNullable(node.get(2)).map(JsonNode::asText).orElse(null),
        Optional.ofNullable(node.get(3)).map(JsonNode::asText).orElse(null));
  }
}
