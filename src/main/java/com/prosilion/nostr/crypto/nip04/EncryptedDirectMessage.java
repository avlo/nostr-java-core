package com.prosilion.nostr.crypto.nip04;

import com.prosilion.nostr.crypto.NostrUtil;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.NonNull;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;

public class EncryptedDirectMessage {
  public static final String AES_CBC_PKCS_5_PADDING = "AES/CBC/PKCS5Padding";
  public static final Cipher CIPHER = Arrays.stream(Security.getProviders())
      .flatMap(provider -> provider.getServices().stream())
      .filter(service -> "Cipher".equals(service.getType()))
      .map(Cipher.class::cast)
      .filter(cipher -> cipher.getAlgorithm().equals(AES_CBC_PKCS_5_PADDING))
      .findFirst().orElseThrow();

  public static String encrypt(@NonNull String message, byte[] senderPrivKey, byte[] rcptPubKey) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    return encryptMessage(senderPrivKey, rcptPubKey, message);
  }

  public static String decryptMessage(byte[] senderPrivKey, @NonNull String encContent, byte[] rcptPubKey) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    SecretKeySpec sharedSecret = getSharedSecretKeySpec(senderPrivKey, rcptPubKey);
    return decryptMessage(sharedSecret, encContent);
  }

  private static String decryptMessage(SecretKeySpec sharedSecretKey, String encodedMessage) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    String[] parts = encodedMessage.split("\\?iv=");

    Base64.Decoder Base64Decoder = Base64.getDecoder();
    byte[] encryptedMessage = Base64Decoder.decode(parts[0]);
    byte[] iv = Base64Decoder.decode(parts[1]);

    IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
    CIPHER.init(Cipher.DECRYPT_MODE, sharedSecretKey, ivParamSpec);

    return new String(CIPHER.doFinal(encryptedMessage), StandardCharsets.UTF_8);
  }

  static String encryptMessage(byte[] senderPrivateKey, byte[] rcptPublicKey, String message) throws NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    SecretKeySpec sharedSecretKey = getSharedSecretKeySpec(senderPrivateKey, rcptPublicKey);

    byte[] iv = NostrUtil.createRandomByteArray(16);
    IvParameterSpec ivParamSpec = new IvParameterSpec(iv);

    CIPHER.init(Cipher.ENCRYPT_MODE, sharedSecretKey, ivParamSpec);

    byte[] msg = message.getBytes(StandardCharsets.UTF_8);
    byte[] encryptedMessage = CIPHER.doFinal(msg);

    Base64.Encoder Base64Encoder = Base64.getEncoder();
    byte[] encryptedMessage64 = Base64Encoder.encode(encryptedMessage);

    byte[] iv64 = Base64Encoder.encode(ivParamSpec.getIV());

    return new String(encryptedMessage64) + "?iv=" + new String(iv64);
  }

  private static SecretKeySpec getSharedSecretKeySpec(byte[] privateKey, byte[] publicKey) {
    final String secKeyHex = NostrUtil.bytesToHex(privateKey);
    final String pubKeyHex = NostrUtil.bytesToHex(publicKey);

    byte[] sharedPoint = getSharedSecret(secKeyHex, pubKeyHex);
    byte[] sharedX = Arrays.copyOfRange(sharedPoint, 1, 33);

    return new SecretKeySpec(sharedX, "AES");
  }

  private static byte[] getSharedSecret(String privateKeyHex, String publicKeyHex) {

    SecP256K1Curve curve = new SecP256K1Curve();
    ECPoint pubKeyPt = curve.decodePoint(NostrUtil.nip04PubKeyHexToBytes("02" + publicKeyHex));
    BigInteger tweakVal = new BigInteger(1, NostrUtil.hexToBytes(privateKeyHex));
    return pubKeyPt.multiply(tweakVal).getEncoded(true);
  }

}
