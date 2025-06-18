package com.prosilion.nostr.codec.serializer;

import com.prosilion.nostr.tag.BaseTag;

public class BaseTagSerializer extends AbstractTagSerializer<BaseTag> {
  public BaseTagSerializer() {
    super(BaseTag.class);
  }
}
