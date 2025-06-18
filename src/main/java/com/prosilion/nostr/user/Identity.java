package com.prosilion.nostr.user;

import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.crypto.schnorr.Schnorr;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.java.Log;

@EqualsAndHashCode
@Log
public class Identity {

    @ToString.Exclude
    @Getter
    private final PrivateKey privateKey;

    private Identity(@NonNull PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Deprecated(forRemoval = true)
    public static Identity getInstance(@NonNull PrivateKey privateKey) {
        return new Identity(privateKey);
    }

    public static Identity create(@NonNull PrivateKey privateKey) {
        return new Identity(privateKey);
    }

    @Deprecated(forRemoval = true)
    public static Identity getInstance(@NonNull String privateKey) {
        return new Identity(new PrivateKey(privateKey));
    }

    public static Identity create(@NonNull String privateKey) {
        return new Identity(new PrivateKey(privateKey));
    }

    /**
     * @return A strong pseudo random identity
     */
    public static Identity generateRandomIdentity() {
        return new Identity(PrivateKey.generateRandomPrivKey());
    }

    public PublicKey getPublicKey() {
        try {
            return new PublicKey(Schnorr.genPubKey(this.getPrivateKey().getRawData()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SneakyThrows
    public Signature sign(@NonNull ISignableEntity signable) {
        final Signature signature = new Signature();
        signature.setRawData(
            Schnorr.sign(
                NostrUtil.sha256(signable.getByteArraySupplier().get().array()),
                this.getPrivateKey().getRawData(),
                generateAuxRand()));
        signature.setPubKey(getPublicKey());
        return signature;
    }

    private byte[] generateAuxRand() {
        return NostrUtil.createRandomByteArray(32);
    }
}
