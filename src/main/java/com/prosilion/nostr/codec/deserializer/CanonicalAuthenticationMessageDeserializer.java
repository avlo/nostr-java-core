package com.prosilion.nostr.codec.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.message.CanonicalAuthenticationMessage;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CanonicalAuthenticationMessageDeserializer extends JsonDeserializer<CanonicalAuthenticationMessage> implements MessageDeserializerIF {
  public static final int NODE_POSITION_AFTER_EVENT_LABEL = 1;
  public static final int CANONICAL_NODE_LENGTH = 2;

  public CanonicalAuthenticationMessageDeserializer() {
    log.info("{} default Ctor() [{}]", getClass().getSimpleName(), this);
  }

  @Override
  public CanonicalAuthenticationMessage deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    return Objects.equals(node.size(), CANONICAL_NODE_LENGTH) ?
        new CanonicalAuthenticationMessage(
            getEvent(node.path(NODE_POSITION_AFTER_EVENT_LABEL)))
        :
        new CanonicalAuthenticationMessage(
            getEvent(node.path(CANONICAL_NODE_LENGTH)),
            node.path(NODE_POSITION_AFTER_EVENT_LABEL).path("subscriptionId").asText());
  }
}
