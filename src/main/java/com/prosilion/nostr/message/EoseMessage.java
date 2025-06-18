package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.prosilion.nostr.enums.Command;
import lombok.Getter;
import org.springframework.lang.NonNull;

@JsonTypeName("EOSE")
public record EoseMessage(
    @Getter String subscriptionId) implements BaseMessage {

  public static Command command = Command.EOSE;

  public EoseMessage(String subscriptionId) {
    this.subscriptionId = BaseMessage.validateSubscriptionId(subscriptionId);
  }

//  @Override
//  public String encode() throws JsonProcessingException {
//    return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(
//        JsonNodeFactory.instance.arrayNode()
//            .add(getCommand().name())
//            .add(getSubscriptionId()));
//  }

  public static <T extends BaseMessage> T decode(@NonNull Object arg) {
    return (T) new EoseMessage(arg.toString());
  }

  @Override
  public Command getCommand() {
    return command;
  }
}
