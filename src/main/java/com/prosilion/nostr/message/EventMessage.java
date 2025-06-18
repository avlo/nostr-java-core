package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.enums.Command;
import com.prosilion.nostr.codec.Encoder;
import com.prosilion.nostr.codec.deserializer.EventMessageDeserializer;
import com.prosilion.nostr.codec.serializer.EventMessageSerializer;
import com.prosilion.nostr.event.GenericEventDtoIF;
import java.util.Objects;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@JsonSerialize(using = EventMessageSerializer.class)
@JsonDeserialize(using = EventMessageDeserializer.class)
public record EventMessage(
    @Getter @JsonPropertyDescription("EVENT") GenericEventDtoIF event,
    @Getter @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String subscriptionId) implements BaseMessage {

  public EventMessage(@JsonProperty GenericEventDtoIF event) {
    this(event, null);
  }

  public EventMessage(@JsonProperty GenericEventDtoIF event, @Nullable String subscriptionId) {
    this.event = event;
    this.subscriptionId = subscriptionId;
  }

  @JsonIgnore
  public static Command command = Command.EVENT;
  @JsonIgnore
  private static final int SIZE_JSON_EVENT_wo_SIG_ID = 2;
  @JsonIgnore
  private static final Function<Object[], Boolean> isEventWoSig = (objArr) ->
      Objects.equals(SIZE_JSON_EVENT_wo_SIG_ID, objArr.length);

  public static BaseMessage decode(@NonNull String json) throws JsonProcessingException {
    return Encoder.ENCODER_MAPPED_AFTERBURNER.readValue(json, EventMessage.class);
  }

  @Override
  @JsonIgnore
  public Command getCommand() {
    return command;
  }
}
