package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.tag.BaseTag;
import java.util.List;

public interface EventIF {
//  GenericEventRecord getGenericEventRecord();

  String getId();

  PublicKey getPublicKey();

  Long getCreatedAt();

  Kind getKind();

  List<BaseTag> getTags();

  String getContent();

  String getSignature();
}
