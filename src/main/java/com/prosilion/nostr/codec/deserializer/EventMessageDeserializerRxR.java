package com.prosilion.nostr.codec.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.EventRxRIF;
import com.prosilion.nostr.event.GenericEventRecordRxR;
import com.prosilion.nostr.message.EventMessageRxR;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

@Slf4j
public class EventMessageDeserializerRxR extends JsonDeserializer<EventMessageRxR> {
  public static final int NODE_POSITION_AFTER_EVENT_LABEL = 1;
  public static final int CANONICAL_NODE_LENGTH = 2;

  public EventMessageDeserializerRxR() {
    log.info("EventMessageDeserializer default Ctor() [{}]", this);
  }

  @Override
  public EventMessageRxR deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    return Objects.equals(node.size(), CANONICAL_NODE_LENGTH) ?
        new EventMessageRxR(
            getEvent(node.path(NODE_POSITION_AFTER_EVENT_LABEL)))
        :
        new EventMessageRxR(
            getEvent(node.path(CANONICAL_NODE_LENGTH)),
            node.path(NODE_POSITION_AFTER_EVENT_LABEL).path("subscriptionId").asText());
  }

  private EventRxRIF getEvent(JsonNode node) {
    return new GenericEventRecordRxR(
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
