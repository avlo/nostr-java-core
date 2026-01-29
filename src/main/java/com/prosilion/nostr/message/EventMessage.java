package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.codec.IDecoder;
import com.prosilion.nostr.codec.deserializer.EventMessageDeserializer;
import com.prosilion.nostr.codec.serializer.EventMessageSerializer;
import com.prosilion.nostr.enums.Command;
import com.prosilion.nostr.event.BaseEvent;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventRecord;
import java.util.Objects;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;
import static com.prosilion.nostr.event.EventIF.DEBUG_PRETTY_PRINTER;

@JsonSerialize(using = EventMessageSerializer.class)
@JsonDeserialize(using = EventMessageDeserializer.class)
@Slf4j
public record EventMessage(
    @Getter @JsonPropertyDescription("EVENT") EventIF event,
    @Getter @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String subscriptionId) implements BaseMessage {

  public static final String INDENT = "  "; //"\t";
  public static final String ALL_NON_QUOTED_WHITESPACE = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";

  @JsonIgnore
  public EventMessage(GenericEventRecord event) {
    this(event, null);
  }

  @JsonIgnore
  public EventMessage(BaseEvent event) {
    this(event.getGenericEventRecord(), null);
  }

  @JsonCreator
  public EventMessage(@JsonProperty EventIF event, @Nullable String subscriptionId) {
    this.event = event;
    this.subscriptionId = subscriptionId;
  }

  @Override
  public String encode() throws JsonProcessingException, NostrException {
    String encodedString = IDecoder.I_DECODER_MAPPER_AFTERBURNER
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(this);
    log.debug("EventMessage encode() encoded string:\n{}", indented(encodedString));
    return replaceWhiteSpace(encodedString);
  }

  public static BaseMessage decode(@NonNull String json) throws JsonProcessingException {
    log.debug("EventMessage decode() decoding incoming json:\n  {}",
        debugPrint(json));
    EventMessage eventMessage = ENCODER_MAPPED_AFTERBURNER.readValue(json, EventMessage.class);
    log.debug("EventMessage decode() returning decoded EventMessage:");
    eventMessage.debug();
    return eventMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    return Objects.equals(
        event,
        ((EventMessage) o).event);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(event);
  }

  @Override
  @JsonIgnore
  public Command getCommand() {
    return Command.EVENT;
  }

  @SneakyThrows
  public void debug() {
    debug(log);
  }

  @SneakyThrows
  public void debug(@NonNull Logger logger) {
    getEvent().debug(logger);
  }

  private static String debugPrint(@NonNull String json) throws JsonProcessingException {
    return
         DEBUG_PRETTY_PRINTER.writerWithDefaultPrettyPrinter()
            .writeValueAsString(
                DEBUG_PRETTY_PRINTER.readValue(json, EventMessage.class));
  }

  private static String indented(String s) {
    return EventIF.indented(s);
  }

  private String replaceWhiteSpace(String s) {
    return s.replaceAll(ALL_NON_QUOTED_WHITESPACE, "");
  }
}

