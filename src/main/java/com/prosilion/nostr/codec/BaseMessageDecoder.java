package com.prosilion.nostr.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.enums.Command;
import com.prosilion.nostr.message.BaseMessage;
import com.prosilion.nostr.message.CanonicalAuthenticationMessage;
import com.prosilion.nostr.message.CloseMessage;
import com.prosilion.nostr.message.ClosedMessage;
import com.prosilion.nostr.message.EoseMessage;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.message.NoticeMessage;
import com.prosilion.nostr.message.OkMessage;
import com.prosilion.nostr.message.ReqMessage;
import com.prosilion.nostr.util.Util;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

@Slf4j
public class BaseMessageDecoder {
  public static final int COMMAND_INDEX = 0;
  public static final int ARG_INDEX = 1;

  public static BaseMessage decode(@NonNull String jsonString) throws JsonProcessingException {
    ValidNostrJsonStructure validNostrJsonStructure = validateProperlyFormedJson(jsonString);
    Object subscriptionId = validNostrJsonStructure.getSubscriptionId();

    return switch (Command.valueOf(validNostrJsonStructure.getCommand())) {
//          client <-> relay messages
      case Command.AUTH ->
//          subscriptionId instanceof Map map ?
         CanonicalAuthenticationMessage.decode(jsonString);
//          RelayAuthenticationMessage.decode(subscriptionId);
      case Command.EVENT -> EventMessage.decode(jsonString);
//            missing client <-> relay handlers
//            case "COUNT" -> CountMessage.decode(subscriptionId);

//            client -> relay messages
      case Command.CLOSE -> CloseMessage.decode(subscriptionId);
      case Command.CLOSED -> ClosedMessage.decode(subscriptionId, jsonString);
      case Command.REQ -> ReqMessage.decode(subscriptionId, jsonString);

//            relay -> client handlers
      case Command.EOSE -> EoseMessage.decode(subscriptionId);
      case Command.NOTICE -> NoticeMessage.decode(subscriptionId);
      case Command.OK -> OkMessage.decode(jsonString);
//            missing relay -> client handlers
//            case "CLOSED" -> Closed.message.decode(subscriptionId);
    };
  }

  private static ValidNostrJsonStructure validateProperlyFormedJson(@NonNull String jsonString) throws JsonProcessingException {
    ValidNostrJsonStructure validNostrJsonStructure = new ValidNostrJsonStructure(
       I_DECODER_MAPPER_AFTERBURNER.readTree(jsonString).get(COMMAND_INDEX).asText(),
       I_DECODER_MAPPER_AFTERBURNER.readTree(jsonString).get(ARG_INDEX).asText());
    log.debug("{} decode(String jsonString) validated incoming json:\n{}", BaseMessageDecoder.class, Util.prettyFormatJson(jsonString, 2));
    return validNostrJsonStructure;
  }

  private record ValidNostrJsonStructure(
     @NonNull String getCommand,
     @NonNull Object getSubscriptionId) {
  }
}
