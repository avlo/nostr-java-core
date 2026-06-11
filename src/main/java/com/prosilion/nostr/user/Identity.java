package com.prosilion.nostr.user;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.crypto.schnorr.Schnorr;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
public class Identity {

  @ToString.Exclude
  @Getter
  private final PrivateKey privateKey;

  private Identity(@NonNull PrivateKey privateKey) {
    this.privateKey = privateKey;
  }

  public static Identity create(@NonNull PrivateKey privateKey) {
    return new Identity(privateKey);
  }

  public static Identity create(@NonNull String privateKey) {
    return create(new PrivateKey(privateKey));
  }

  /**
   * @return A strong pseudo random identity
   */
  public static Identity generateRandomIdentity() {
    return new Identity(PrivateKey.generateRandomPrivKey());
  }

  public final PublicKey getPublicKey() {
    try {
      return new PublicKey(Schnorr.genPubKey(this.getPrivateKey().getRawData()));
    } catch (Exception e) {
      throw new NostrException("Schnorr.genPubKey failed with error: ", e);
    }
  }

  public final Signature sign(@NonNull ISignableEntity signable) {
    try {
      return
         new Signature(
            Schnorr.sign(
               NostrUtil.sha256(signable.getByteArraySupplier().get().array()),
               this.getPrivateKey().getRawData(),
               generateAuxRand())
         );
    } catch (Exception e) {
      throw new NostrException("Schnorr.sign failed with error: ", e);
    }
  }

  private byte[] generateAuxRand() {
    return NostrUtil.createRandomByteArray(32);
  }
}
