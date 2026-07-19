package com.prosilion.nostr.tag;

import com.prosilion.nostr.user.PublicKey;

public interface SetsPairedEventTagIF {
  String getEventId();
  PublicKey getPublicKey();
}
