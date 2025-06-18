package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.crypto.HexStringValidator;
import com.prosilion.nostr.crypto.bech32.Bech32;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import com.prosilion.nostr.tag.BaseTag;
import java.beans.Transient;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;

public record GenericEventDto(
    @Getter String id,
    @Getter @JsonProperty("pubkey") PublicKey publicKey,
    @Getter @JsonProperty("created_at") @EqualsAndHashCode.Exclude Long createdAt,
    @Getter @EqualsAndHashCode.Exclude Kind kind,
    @Getter @EqualsAndHashCode.Exclude @JsonProperty("tags") List<BaseTag> tags,
    @Getter @EqualsAndHashCode.Exclude String content,
    @Getter @JsonProperty("sig") @EqualsAndHashCode.Exclude Signature signature) implements GenericEventDtoIF {
  @JsonIgnore
  private static final Log log = LogFactory.getLog(GenericEventDto.class);

  public GenericEventDto(String id) {
    this(id, null, null, null, new ArrayList<>(), null, null);
  }
//
//  public GenericEventDto(String id, PublicKey publicKey, Long createdAt, Kind kind, Signature signature) {
//    this(id, publicKey, createdAt, kind, new ArrayList<>(), "", signature);
//  }
//
//  public GenericEventDto(String id, PublicKey publicKey, Long createdAt, Kind kind, List<BaseTag> tags, Signature signature) throws NostrException, NoSuchAlgorithmException {
//    this(id, publicKey, createdAt, kind, tags, "", signature);
//  }

  @JsonCreator
  public GenericEventDto(String id, PublicKey publicKey, Long createdAt, Kind kind, List<BaseTag> tags, String content, Signature signature) {
    this.id = validateId(id);
    this.publicKey = publicKey;
    this.kind = kind;
    this.tags = tags;
    this.createdAt = createdAt;
    this.content = content;
    this.signature = signature;
  }

  private String validateId(@NonNull String id) {
    HexStringValidator.validateHex(id, 64);
    return id;
  }

  @Override
  public String toBech32() {
    try {
      return Bech32.toBech32(Bech32Prefix.NOTE, this.getId());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

//  @JsonIgnore
//  @EqualsAndHashCode.Exclude
//  private byte[] _serializedEvent;

  @Transient
  @Override
  public Supplier<ByteBuffer> getByteArraySupplier() throws NostrException {
    byte[] serializedEvent = serialize().getBytes(StandardCharsets.UTF_8);
    log.info(String.format("Serialized event: %s", new String(serializedEvent)));
    return () -> ByteBuffer.wrap(serializedEvent);
  }
}
