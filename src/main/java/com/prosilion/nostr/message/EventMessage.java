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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;

@JsonSerialize(using = EventMessageSerializer.class)
@JsonDeserialize(using = EventMessageDeserializer.class)
public record EventMessage(
    @Getter @JsonPropertyDescription("EVENT") EventIF event,
    @Getter @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String subscriptionId) implements BaseMessage {

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
    return IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(this);
  }

  public static BaseMessage decode(@NonNull String json) throws JsonProcessingException {
    return ENCODER_MAPPED_AFTERBURNER.readValue(json, EventMessage.class);
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
}
