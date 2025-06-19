package com.prosilion.nostr;

import com.prosilion.nostr.crypto.bech32.Bech32;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoTest {
  @Test
  public void testBech32() throws Exception {
    String npub = "npub126klq89p42wk78p4j5ur8wlxmxdqepdh8tez9e4axpd4run5nahsmff27j";
    assertEquals(npub, Bech32.toBech32(Bech32Prefix.NPUB, "56adf01ca1aa9d6f1c35953833bbe6d99a0c85b73af222e6bd305b51f2749f6f"));
    assertEquals("56adf01ca1aa9d6f1c35953833bbe6d99a0c85b73af222e6bd305b51f2749f6f", Bech32.fromBech32(npub));
  }

//  @Test
//  public void testVerifySignature() throws NostrException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
//    System.out.println("testVerifySignature");
//
//    Identity identity = Identity.generateRandomIdentity();
//    BaseEvent event = new TextNoteEvent(identity, "Hello World");
//
//    boolean verification = Schnorr.verify(event., identity.getPublicKey().getRawData(), signature.getRawData());
//    assertTrue(verification, "Schnorr must have a true verify result.");
//
//    event[0] = createTextNoteEvent(identity.getPublicKey(), "Guten Tag");
//    event[0].update();
//    message = NostrUtil.sha256(event[0].get_serializedEvent());
//    verification = Schnorr.verify(message, identity.getPublicKey().getRawData(), signature.getRawData());
//
//    assertFalse(verification);
//  }
}
