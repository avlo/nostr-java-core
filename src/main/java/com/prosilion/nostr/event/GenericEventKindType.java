package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prosilion.nostr.crypto.HexStringValidator;
import com.prosilion.nostr.crypto.bech32.Bech32;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.KindTypeIF;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.beans.Transient;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

// TODO: common code w/ GenericEventKind, needs cleanup
@Slf4j
public record GenericEventKindType(
    @Getter String id,
    @Getter @JsonProperty("pubkey") PublicKey publicKey,
    @Getter @JsonProperty("created_at") @EqualsAndHashCode.Exclude Long createdAt,
    @Getter @EqualsAndHashCode.Exclude Kind kind,
    @Getter @EqualsAndHashCode.Exclude @JsonProperty("tags") List<BaseTag> tags,
    @Getter @EqualsAndHashCode.Exclude String content,
    @Getter @JsonProperty("sig") @EqualsAndHashCode.Exclude Signature signature,
    @Getter @EqualsAndHashCode.Exclude @JsonIgnore KindTypeIF kindType) implements GenericEventKindTypeIF {

  public GenericEventKindType {
    HexStringValidator.validateHex(id, 64);
  }

  @Override
  public String toBech32() {
//    TODO: cleanup below
    try {
      return Bech32.toBech32(Bech32Prefix.NOTE, this.getId());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Transient
  @Override
  public Supplier<ByteBuffer> getByteArraySupplier() throws NostrException {
    byte[] serializedEvent = serialize().getBytes(StandardCharsets.UTF_8);
    log.debug(String.format("Serialized GenericEventKindType event: %s", new String(serializedEvent)));
    return () -> ByteBuffer.wrap(serializedEvent);
  }
}
