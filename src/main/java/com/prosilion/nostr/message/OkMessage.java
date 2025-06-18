package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.enums.Command;
import lombok.Getter;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

@JsonTypeName("OK")
public record OkMessage(
    @Getter String eventId,
    @Getter Boolean flag,
    @Getter String message) implements BaseMessage {
  public static Command command = Command.OK;

//  @Override
//  public String encode() throws JsonProcessingException {
//    return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(
//        JsonNodeFactory.instance.arrayNode()
//            .add(getCommand().name())
//            .add(getEventId())
//            .add(getFlag())
//            .add(getMessage()));
//  }

  public static <T extends BaseMessage> T decode(@NonNull String jsonString) {
    try {
      Object[] msgArr = I_DECODER_MAPPER_AFTERBURNER.readValue(jsonString, Object[].class);
      return (T) new OkMessage(msgArr[1].toString(), (Boolean) msgArr[2], msgArr[3].toString());
    } catch (JsonProcessingException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public Command getCommand() {
    return command;
  }
}
