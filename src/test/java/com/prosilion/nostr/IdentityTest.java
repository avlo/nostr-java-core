package com.prosilion.nostr;

import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.event.BaseEvent;
import com.prosilion.nostr.event.TextNoteEvent;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdentityTest {

  public IdentityTest() {
  }

  @Test
  public void testSignEvent() throws NostrException, NoSuchAlgorithmException {
    System.out.println("testSignEvent");
    Identity identity = Identity.generateRandomIdentity();
    PublicKey publicKey = identity.getPublicKey();
    BaseEvent instance = new TextNoteEvent(identity, "some content");
    Assertions.assertNotNull(instance.getSignature());
  }

//  @Test
//  public void testSignDelegationTag() {
//    System.out.println("testSignDelegationTag");
//    Identity identity = Identity.generateRandomIdentity();
//    PublicKey publicKey = identity.getPublicKey();
//    DelegationTag delegationTag = new DelegationTag(publicKey, null);
//    identity.sign(delegationTag);
//    Assertions.assertNotNull(delegationTag.getSignature());
//  }
    
    
/*
    @Test
    public void testDecryptMessage() {
        try {
            System.out.println("testDecryptMessage");
            var senderPublicKey = Identity.getInstance().getPublicKey();
            
            PrivateKey rcptSecKey = new PrivateKey(NostrUtil.hexToBytes(Bech32.fromBech32("nsec13sntjjh35dd4u3lwy42lnpszydmkwar708y3jzwxr937fy2q73hsmvez4z")));
            PublicKey rcptPubKey = new PublicKey("edd898fc2817ee64f7ee1941d193d53c2daa77db4b8409240565fc9644626878");

            final DirectMessageEvent dmEvent = EntityFactory.Events.createDirectMessageEvent(senderPublicKey, rcptPubKey, "Hello uq7yfx3l!");

            new IdentityHelper(Identity.getInstance()).encryptDirectMessage(dmEvent);

            var rcptId = new Identity(rcptSecKey);
            var msg = new IdentityHelper(rcptId).decryptMessage(dmEvent.getContent(), dmEvent.getPubKey());
            
            Assertions.assertEquals("Hello uq7yfx3l!", msg);
        } catch (NostrException ex) {
            Assertions.fail(ex);
        }
    }
*/

}
