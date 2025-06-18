package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.prosilion.nostr.codec.Encoder;
import com.prosilion.nostr.codec.FiltersEncoder;
import com.prosilion.nostr.message.ReqMessage;
import java.io.IOException;

public class ReqMessageSerializer<T extends ReqMessage> extends StdSerializer<T> {
  public ReqMessageSerializer() {
    super((Class<T>) ReqMessage.class);
  }

  public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    ArrayNode encoderArrayNode = JsonNodeFactory.instance.arrayNode();
    encoderArrayNode
        .add(value.getCommand().getName())
        .add(value.getSubscriptionId());

    FiltersEncoder filtersEncoder = new FiltersEncoder();
    
    value.getFiltersList().stream()
        .map(filtersEncoder::encode)
        .map(Encoder::createJsonNode)
        .forEach(encoderArrayNode::add);

    gen.writePOJO(encoderArrayNode);
  }

  protected void applyCustomAttributes(ObjectNode node, T value) {
  }
}
