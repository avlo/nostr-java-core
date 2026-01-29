package com.prosilion.nostr.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;
import static com.prosilion.nostr.message.EventMessage.INDENT;

public interface EventIF extends Serializable {
  ObjectMapper DEBUG_PRETTY_PRINTER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  default String serialize() throws NostrException {
    var arrayNode = JsonNodeFactory.instance.arrayNode();

    try {
      arrayNode.add(0);
      arrayNode.add(getPublicKey().toString());
      arrayNode.add(getCreatedAt());
      arrayNode.add(getKind().getValue());
      arrayNode.add(ENCODER_MAPPED_AFTERBURNER.valueToTree(getTags()));
      arrayNode.add(getContent());

      return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(arrayNode);
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

  @SneakyThrows
  static String createPrettyPrintJson(@NonNull GenericEventRecord s) {
    return indented(DEBUG_PRETTY_PRINTER.writeValueAsString(s));
  }

  @SneakyThrows
  default String createPrettyPrintJson() {
    return indented(DEBUG_PRETTY_PRINTER.writeValueAsString(asGenericEventRecord.apply(this)));
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

  Function<EventIF, String> createPrettyPrintJson = eventIF ->
      createPrettyPrintJson(eventIF.asGenericEventRecord());

  //  @SneakyThrows
//  static String createPrettyPrintJson(@NonNull GenericEventRecord s, String message) {
//    return DEBUG_PRETTY_PRINTER.writeValueAsString(s);
//  }

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

  static String indented(@NonNull String s) {
    return s.replaceAll("(?m)^", INDENT);
  }
}
