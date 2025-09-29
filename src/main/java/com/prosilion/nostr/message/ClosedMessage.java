package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.enums.Command;
import lombok.Getter;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;
import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

@JsonTypeName("CLOSED")
public record ClosedMessage(@Getter String subscriptionId, @Getter String message) implements BaseMessage {
  public ClosedMessage(@NonNull String subscriptionId, @NonNull String message) {
    this.subscriptionId = BaseMessage.validateSubscriptionId(subscriptionId);
    this.message = message;
  }

  @Override
  public String encode() throws JsonProcessingException {
    return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(
        JsonNodeFactory.instance.arrayNode()
            .add(getCommand().name())
            .add(getSubscriptionId())
            .add(getMessage()));
  }

  public static <T extends BaseMessage> T decode(@NonNull Object subscriptionId, @NonNull String jsonString) throws JsonProcessingException {
    Object[] msgArr = I_DECODER_MAPPER_AFTERBURNER.readValue(jsonString, Object[].class);
    return (T) new ClosedMessage(subscriptionId.toString(), msgArr[2].toString());
  }

  @Override
  public Command getCommand() {
    return Command.CLOSED;
  }
}
