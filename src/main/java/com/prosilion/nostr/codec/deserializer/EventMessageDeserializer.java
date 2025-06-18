package com.prosilion.nostr.codec.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.event.GenericEventDto;
import com.prosilion.nostr.user.Signature;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.tag.BaseTag;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

public class EventMessageDeserializer extends JsonDeserializer<EventMessage> {
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

  private static GenericEventDto getEvent(JsonNode node) {
    return
        new GenericEventDto(
            node.path("id").asText(),
            new PublicKey(
                node.path("pubkey").asText()),
            Long.valueOf(
                node.path("created_at").asText()),
            Kind.valueOf(
                node.path("kind").asInt()),
            Arrays.asList(
                I_DECODER_MAPPER_AFTERBURNER.convertValue(
                    node.path("tags"),
                    BaseTag[].class)),
            node.path("content").asText(),
            Signature.fromString(
                node.path("sig").asText()));
  }
}
