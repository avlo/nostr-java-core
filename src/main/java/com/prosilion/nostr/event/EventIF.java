package com.prosilion.nostr.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import com.prosilion.nostr.util.Util;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.springframework.lang.NonNull;

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
      throw new NostrException(e);
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
    return Util.prettyFormatJson(serialize(genericEventRecord));
  }

  default String createPrettyPrintJson() {
    return createPrettyPrintJson(
        asGenericEventRecord.apply(this));
  }

  Function<EventIF, String> createPrettyPrintJson = eventIF ->
      createPrettyPrintJson(eventIF.asGenericEventRecord());
}
