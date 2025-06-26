package com.prosilion.nostr.user;

import com.prosilion.nostr.NostrException;

public interface IBech32Encodable {
    String toBech32() throws NostrException;
}
