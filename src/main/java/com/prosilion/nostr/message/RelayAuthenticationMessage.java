package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.prosilion.nostr.enums.Command;
import lombok.Getter;
import org.springframework.lang.NonNull;

@JsonTypeName("AUTH")
public record RelayAuthenticationMessage(
    @Getter String challenge) implements BaseMessage {
  public static Command command = Command.AUTH;

  public RelayAuthenticationMessage(String challenge) {
    this.challenge = challenge;
  }

//  @Override
//  public String encode() throws JsonProcessingException {
//    return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(
//        JsonNodeFactory.instance.arrayNode()
//            .add(getCommand().name())
//            .add(getChallenge()));
//  }

  public static <T extends BaseMessage> T decode(@NonNull Object arg) {
    return (T) new RelayAuthenticationMessage(arg.toString());
  }

  @Override
  public Command getCommand() {
    return command;
  }
}
