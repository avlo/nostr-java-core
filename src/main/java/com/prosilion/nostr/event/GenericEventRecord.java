package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.crypto.HexStringValidator;
import com.prosilion.nostr.crypto.bech32.Bech32;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.ISignableEntity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.beans.Transient;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.NonNull;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;

public record GenericEventRecord(
    @Getter
    String id,

    @JsonProperty("pubkey")
    PublicKey publicKey,

    @JsonProperty("created_at")
    Long createdAt,

    @Getter
    Kind kind,

    @Getter
    @JsonProperty("tags")
    List<BaseTag> tags,

    @Getter
    String content,

    @JsonProperty("sig")
    Signature signature) implements
//    TagMappedEventIF,
    EventIF,
    IEvent, ISignableEntity {

  public GenericEventRecord {
    id = HexStringValidator.validateHex(id, 64);
    tags = Optional.ofNullable(tags).orElse(new ArrayList<>());
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
    return () -> ByteBuffer.wrap(serializedEvent);
  }

  private String serialize() throws NostrException {
    var arrayNode = JsonNodeFactory.instance.arrayNode();

    try {
      arrayNode.add(0);
      arrayNode.add(getPublicKey().toString());
      arrayNode.add(getCreatedAt());
      arrayNode.add(getKind().getValue());
      arrayNode.add(ENCODER_MAPPED_AFTERBURNER.valueToTree(getTags()));
      arrayNode.add(getContent());

      String s = ENCODER_MAPPED_AFTERBURNER.writeValueAsString(arrayNode);
      return s;
    } catch (JsonProcessingException e) {
      throw new NostrException(e);
    }
  }

  @Override
  public boolean equals(@NonNull Object o) {
    if (getClass() != o.getClass()) return false;
    GenericEventRecord that = (GenericEventRecord) o;
    return
        // test tags first, also HashSet for fast list equality    
        new HashSet<>(tags).containsAll(that.tags) &&
            Objects.equals(id, that.id) &&
            Objects.equals(kind, that.kind) &&
//            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(content, that.content) &&
            Objects.equals(publicKey, that.publicKey) &&
            Objects.equals(signature, that.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, publicKey,
//        createdAt, 
        kind,
        new HashSet<>(tags),
        content, signature);
  }

  @JsonProperty("pubkey")
  public PublicKey getPublicKey() {
    return publicKey;
  }

  @JsonProperty("created_at")
  public Long getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("sig")
  public Signature getSignature() {
    return signature;
  }
}
