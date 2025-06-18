package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.prosilion.nostr.codec.Encoder;
import com.prosilion.nostr.tag.BaseTag;
import java.io.IOException;
import org.apache.commons.lang3.stream.Streams;

abstract class AbstractTagSerializer<T extends BaseTag> extends StdSerializer<T> implements Encoder<T> {

  protected AbstractTagSerializer(Class<T> t) {
    super(t);
  }

  public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    final ObjectNode node = getBaseTagEncoderMappedAfterburnerCopy(this)
        .getNodeFactory().objectNode();

    Streams.failableStream(
        value.getSupportedFields().stream()).forEach(f ->
        value.getFieldValue(f)
            .ifPresent(s ->
                node.put(f.getName(), s)));

    applyCustomAttributes(node, value);

    ArrayNode arrayNode = node.objectNode().putArray("values").add(value.getCode());
    node.properties().iterator().forEachRemaining(entry -> arrayNode.add(entry.getValue().asText()));
    gen.writePOJO(arrayNode);
  }

  @Override
  public String encode(T value) throws JsonProcessingException {
    return writeValueAsString(value, this);
  }

  protected void applyCustomAttributes(ObjectNode node, T value) {
  }
}
