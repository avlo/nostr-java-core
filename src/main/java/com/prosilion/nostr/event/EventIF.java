package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import com.prosilion.nostr.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.slf4j.Logger;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;

public interface EventIF extends Serializable {
  //  public static final String ALL_NON_QUOTED_WHITESPACE = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";

  static String serialize(GenericEventRecord genericEventRecord) throws NostrException {
    var arrayNode = JsonNodeFactory.instance.arrayNode();

    try {
      arrayNode.add(genericEventRecord.getId());
      arrayNode.add(genericEventRecord.getPublicKey().toString());
      arrayNode.add(genericEventRecord.getCreatedAt());
      arrayNode.add(genericEventRecord.getKind().getValue());
      arrayNode.add(ENCODER_MAPPED_AFTERBURNER.valueToTree(genericEventRecord.getTags()));
      arrayNode.add(genericEventRecord.getContent());
      arrayNode.add(genericEventRecord.getSignature().toString());

      String s = ENCODER_MAPPED_AFTERBURNER.writeValueAsString(arrayNode);
      return s;
    } catch (JsonProcessingException e) {
      throw new NostrException("serialize(GenericEventRecord genericEventRecord) failed with JsonProcessingException:", e);
    }
  }

  String getId();

  PublicKey getPublicKey();

  Long getCreatedAt();

  Kind getKind();

  List<BaseTag> getTags();

  String getContent();

  Signature getSignature();

  default GenericEventRecord asGenericEventRecord() {
    return asGenericEventRecord.apply(this);
  }

  Function<EventIF, GenericEventRecord> asGenericEventRecord = eventIF ->
     new GenericEventRecord(
        eventIF.getId(),
        eventIF.getPublicKey(),
        eventIF.getCreatedAt(),
        eventIF.getKind(),
        eventIF.getTags(),
        eventIF.getContent(),
        eventIF.getSignature());

  /**
   * (common) speed optimized tag-getting variants
   */
  @JsonIgnore
  default <T extends BaseTag> List<T> getTypeSpecificTags(@NonNull Class<T> tagClass) {
    var tags = this.getTags();
    var result = new ArrayList<T>(Math.min(tags.size(), 4));
    for (var tag : tags) {
      if (tagClass.isInstance(tag)) {
        result.add(tagClass.cast(tag));
      }
    }
    return result;
  }

  @JsonIgnore
  default Optional<RelayTag> findRelayTag() {
    return findFirstTag(RelayTag.class);
  }

  @JsonIgnore
  default RelayTag requireRelayTag() {
    return requireFirstTag(RelayTag.class);
  }

  @JsonIgnore
  String MISSING_RELAY_TAG_URL = "RelayTag: [%s] missing required url";

  @JsonIgnore
  default String requireRelayTagUrl() {
    Relay relay = requireRelayTag().requireRelay();
    if (relay.getUrl() == null) {
      throw new NostrException(String.format(
         MISSING_RELAY_TAG_URL, this.createPrettyPrintJson()));
    }
    return relay.getUrl();
  }

  default <T extends BaseTag> Optional<T> findFirstTag(@NonNull Class<T> clazz) {
    for (BaseTag tag : this.getTags()) {
      if (clazz.isInstance(tag)) {
        return Optional.of(clazz.cast(tag));
      }
    }
    return Optional.empty();
  }

  default <T extends BaseTag> T requireFirstTag(@NonNull Class<T> clazz) {
    return findFirstTag(clazz).orElseThrow(() ->
       new NostrException(String.format("eventIF:\n%s\nis missing required [%s] tag",
          this.createPrettyPrintJson(),
          clazz.getSimpleName())));
  }

  default void debug(@NonNull Logger logger) {
    Stream.of(this)
       .collect(
          Collectors.toMap(
             EventIF::getClass,
             EventIF::asGenericEventRecord))
       .forEach((eventIFClass, serializedGenericEventRecord) ->
          logger.debug(
             "EventIF is of Class type:\n  {}\nwith serialized Event Content:\n{}",
             eventIFClass.getSimpleName(),
             createPrettyPrintJson(serializedGenericEventRecord)));
  }

  static String createPrettyPrintJson(@NonNull GenericEventRecord genericEventRecord) {
    try {
      return Util.prettyFormatJson(serialize(genericEventRecord), 2);
    } catch (NostrException e) {
      String message = "prettyPrint serialization failed.  since cosmetic only for debugs, just return genericEventRecord.toString():\n  ";
      return message.concat(genericEventRecord.toString());
    }
  }

  default String createPrettyPrintJson() {
    return createPrettyPrintJson(
       asGenericEventRecord.apply(this));
  }

  Function<EventIF, String> createPrettyPrintJson = eventIF ->
     createPrettyPrintJson(eventIF.asGenericEventRecord());
}
