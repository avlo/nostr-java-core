package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.prosilion.nostr.enums.Command;
import lombok.Getter;
import org.springframework.lang.NonNull;

@JsonTypeName("NOTICE")
public record NoticeMessage(
    @Getter @JsonProperty String message) implements BaseMessage {
  public static Command command = Command.NOTICE;

//  @Override
//  public String encode() throws JsonProcessingException {
//    return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(
//        JsonNodeFactory.instance.arrayNode()
//            .add(getCommand().name())
//            .add(getMessage()));
//  }

  public static <T extends BaseMessage> T decode(@NonNull Object arg) {
    return (T) new NoticeMessage(arg.toString());
  }

  @Override
  public Command getCommand() {
    return command;
  }
}
