package com.prosilion.nostr.codec.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.message.EventMessage;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventMessageDeserializer extends JsonDeserializer<EventMessage> implements MessageDeserializerIF {
  public static final int NODE_POSITION_AFTER_EVENT_LABEL = 1;
  public static final int CANONICAL_NODE_LENGTH = 2;

  @Override
  public EventMessage deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    return Objects.equals(node.size(), CANONICAL_NODE_LENGTH) ?
        new EventMessage(
            getEvent(node.path(NODE_POSITION_AFTER_EVENT_LABEL)))
        :
        new EventMessage(
            getEvent(node.path(CANONICAL_NODE_LENGTH)),
            node.path(NODE_POSITION_AFTER_EVENT_LABEL).path("subscriptionId").asText());
  }
}
