package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prosilion.nostr.crypto.HexStringValidator;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

public record GenericEventRecord(
    @Getter
    String id,

    @Getter
    @JsonProperty("pubkey")
    PublicKey publicKey,

    @Getter
    @JsonProperty("created_at")
    Long createdAt,

    @Getter
    Kind kind,

    @Getter
    @JsonProperty("tags")
    List<BaseTag> tags,

    @Getter
    String content,

    @Getter
    @JsonProperty("sig")
    Signature signature) {

  public GenericEventRecord {
    id = HexStringValidator.validateHex(id, 64);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    GenericEventRecord that = (GenericEventRecord) o;
    return Objects.equals(id, that.id) && Objects.equals(kind, that.kind) && Objects.equals(createdAt, that.createdAt) && Objects.equals(content, that.content) && Objects.equals(publicKey, that.publicKey) && Objects.equals(signature, that.signature) &&
        // fast list equality    
        new HashSet<>(tags).containsAll(that.tags)
        ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, publicKey, createdAt, kind,
        new HashSet<>(tags),
        content, signature);
  }
}
