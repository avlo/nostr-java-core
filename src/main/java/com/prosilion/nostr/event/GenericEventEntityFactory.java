package com.prosilion.nostr.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.user.ISignableEntity;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.tag.BaseTag;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;
import lombok.NonNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;

class GenericEventEntityFactory {
  private static final Log log = LogFactory.getLog(GenericEventEntityFactory.class);

  protected static GenericEventRecord createInstance(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull List<BaseTag> tags,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {

    long epochSecond = Instant.now().getEpochSecond();
    GenericEventRecordFlux flux = new GenericEventRecordFlux(
        identity.getPublicKey(),
        epochSecond,
        kind, tags, content);

    GenericEventRecord genericEventRecord = new GenericEventRecord(
        NostrUtil.bytesToHex(NostrUtil.sha256(flux.getByteArraySupplier().get().array())),
        identity.getPublicKey(),
        epochSecond,
        kind,
        tags,
        content,
        identity.sign(flux));

    log.debug(String.format("\nGenericEventRecord created:\n  %s\n", genericEventRecord));

    return genericEventRecord;
  }

  private record GenericEventRecordFlux(
      PublicKey pubkey,
      Long createdAt,
      Kind kind,
      List<BaseTag> tags,
      String content) implements ISignableEntity {

    public Supplier<ByteBuffer> getByteArraySupplier() throws NostrException {
      byte[] serializedEvent = serialize().getBytes(StandardCharsets.UTF_8);
      return () -> ByteBuffer.wrap(serializedEvent);
    }

    public String serialize() throws NostrException {
      var arrayNode = JsonNodeFactory.instance.arrayNode();

      try {
        arrayNode.add(0);
        arrayNode.add(pubkey.toString());
        arrayNode.add(createdAt);
        arrayNode.add(kind.getValue());
        arrayNode.add(ENCODER_MAPPED_AFTERBURNER.valueToTree(tags));
        arrayNode.add(content);

        return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(arrayNode);
      } catch (JsonProcessingException e) {
        throw new NostrException(e);
      }
    }
  }
}
