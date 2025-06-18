package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.crypto.HexStringValidator;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import com.prosilion.nostr.tag.BaseTag;
import java.util.List;
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
}
