package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.enums.Command;
import lombok.Getter;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;

@JsonTypeName("CLOSE")
public record CloseMessage(
    @Getter String subscriptionId) implements BaseMessage {
  public static Command command = Command.CLOSE;

  public CloseMessage(String subscriptionId) {
    this.subscriptionId = BaseMessage.validateSubscriptionId(subscriptionId);
  }

  @Override
  public String encode() throws JsonProcessingException {
    return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(
        JsonNodeFactory.instance.arrayNode()
            .add(getCommand().name())
            .add(getSubscriptionId()));
  }

  public static <T extends BaseMessage> T decode(@NonNull Object arg) {
    return (T) new CloseMessage(arg.toString());
  }

  @Override
  public Command getCommand() {
    return command;
  }
}
