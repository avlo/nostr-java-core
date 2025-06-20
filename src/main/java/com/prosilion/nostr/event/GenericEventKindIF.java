package com.prosilion.nostr.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.user.ISignableEntity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import com.prosilion.nostr.tag.BaseTag;
import java.util.List;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;

public interface GenericEventKindIF extends IEvent, ISignableEntity {
  default String serialize() throws NostrException {
    var arrayNode = JsonNodeFactory.instance.arrayNode();

    try {
      arrayNode.add(0);
      arrayNode.add(getPublicKey().toString());
      arrayNode.add(getCreatedAt());
      arrayNode.add(getKind().getValue());
      arrayNode.add(ENCODER_MAPPED_AFTERBURNER.valueToTree(getTags()));
      arrayNode.add(getContent());

      return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(arrayNode);
    } catch (JsonProcessingException e) {
      throw new NostrException(e);
    }
  }

  String getId();
  PublicKey getPublicKey();
  Long getCreatedAt();
  Kind getKind();
  List<BaseTag> getTags();
  String getContent();
  Signature getSignature();

  String toBech32();
  String toString();
  boolean equals(Object o);

  //  boolean canEqual(Object other);
  int hashCode();

//  void setKind(Kind kind);
}
