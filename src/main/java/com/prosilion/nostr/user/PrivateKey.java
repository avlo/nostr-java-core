package com.prosilion.nostr.user;

import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import com.prosilion.nostr.crypto.schnorr.Schnorr;

public class PrivateKey extends BaseKey {

    public PrivateKey(byte[] rawData) {
        super(KeyType.PRIVATE, rawData, Bech32Prefix.NSEC);
    }

    public PrivateKey(String hexPrivKey) {
        super(KeyType.PRIVATE, NostrUtil.hexToBytes(hexPrivKey), Bech32Prefix.NSEC);
    }

    public static PrivateKey generateRandomPrivKey() {
        return new PrivateKey(Schnorr.generatePrivateKey());
    }
}
