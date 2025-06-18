package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prosilion.nostr.tag.GenericTag;

public class GenericTagSerializer extends AbstractTagSerializer<GenericTag> {
  public GenericTagSerializer() {
    super(GenericTag.class);
  }

  @Override
  protected void applyCustomAttributes(ObjectNode node, GenericTag value) {
    value.getAttributes().forEach(a -> node.put(a.getName(), a.getValue().toString()));
  }
}
