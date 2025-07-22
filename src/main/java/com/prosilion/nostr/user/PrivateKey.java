package com.prosilion.nostr.user;

import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import com.prosilion.nostr.crypto.schnorr.Schnorr;

public final class PrivateKey {
    
    private final BaseKey baseKey;

    public PrivateKey(byte[] rawData) {
        baseKey = new BaseKey(KeyType.PRIVATE, rawData, Bech32Prefix.NSEC);
    }

    public PrivateKey(String hexPrivKey) {
        baseKey = new BaseKey(KeyType.PRIVATE, NostrUtil.hexToBytes(hexPrivKey), Bech32Prefix.NSEC);
    }

    public byte[] getRawData() {
        return baseKey.getRawData();
    }

    @Override
    public int hashCode() {
        return baseKey.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        // null check
        if (o == null)
            return false;

        // type check and cast
        if (getClass() != o.getClass())
            return false;

      return baseKey.equals(((PrivateKey) o).baseKey);
    }

    public static PrivateKey generateRandomPrivKey() {
        return new PrivateKey(Schnorr.generatePrivateKey());
    }
}
