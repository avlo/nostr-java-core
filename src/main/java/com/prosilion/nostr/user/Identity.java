package com.prosilion.nostr.user;

import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.crypto.schnorr.Schnorr;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.lang.NonNull;

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

  public PublicKey getPublicKey() {
    try {
      return new PublicKey(Schnorr.genPubKey(this.getPrivateKey().getRawData()));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @SneakyThrows
  public Signature sign(@NonNull ISignableEntity signable) {
    return
        new Signature(
            Schnorr.sign(
                NostrUtil.sha256(signable.getByteArraySupplier().get().array()),
                this.getPrivateKey().getRawData(),
                generateAuxRand())
        );
  }

  private byte[] generateAuxRand() {
    return NostrUtil.createRandomByteArray(32);
  }
}
